package cn.joker.controller.taskcontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.stdName;
import cn.joker.sevice.ReportService;
import cn.joker.sevice.TagService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private ReportService reportService;
    @Resource
    private UserService userService;
    @Resource
    private TagService tagService;


    /**
     * 发起任务
     *
     * @param request  http
     * @param response http
     * @return 任务是否发布成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/releaseTask")
    public void releaseTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity task = new TaskEntity();
        task.setTaskName(jsonObject.getString(stdName.TASKNAME));
        task.setDescription(jsonObject.getString(stdName.DESCRIPTION));
        String endDate = jsonObject.getString(stdName.ENDDATE);
        String startDate = jsonObject.getString(stdName.STARTDATE);
        task.setStartDate(DateHelper.convertStringToDate(startDate));
        task.setEndDate(DateHelper.convertStringToDate(endDate));
        task.setExpectedNumber(jsonObject.getInt(stdName.EXCEPTEDNUMBER));
        task.setPoints(jsonObject.getInt(stdName.POINTS));
        UserEntity userEntity = userService.findByUsername(jsonObject.getString(stdName.SPONSORNAME));
        task.setSponsor(userEntity);
        userEntity.setPoints(userEntity.getPoints() - task.getPoints() * task.getExpectedNumber());

        JSONArray tagArray = jsonObject.getJSONArray(stdName.TAG);
        List<TagEntity> tagEntities = new ArrayList<>();

        addTag(tagArray, tagEntities);
        task.setTagEntityList(tagEntities);
        task.setImageNum(0);
        task.setTaskName(jsonObject.getString(stdName.TASKNAME));
        task.setWorkerLevel(jsonObject.getInt(stdName.WORKERLEVEL));
        task.setState(1);
        task.setCompletedNumber(0);
        JSONObject ret = new JSONObject();

        ret.put(stdName.MES, userService.modify(userEntity));
        ret.put(stdName.TASKID, taskService.releaseTask(task));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 修改任务
     *
     * @param request http
     * @return 任务是否修改
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modifyTask")
    public void modifyTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity task = (TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID));
        task.setDescription(jsonObject.getString(stdName.DESCRIPTION));
        task.setEndDate(DateHelper.convertStringToDate(jsonObject.getString(stdName.ENDDATE)));
        JSONArray tagArray = jsonObject.getJSONArray(stdName.TAG);
        List<TagEntity> tagEntities = new ArrayList<>();

        addTag(tagArray, tagEntities);
        task.setTagEntityList(tagEntities);
        task.setExpectedNumber(jsonObject.getInt(stdName.EXCEPTEDNUMBER));
        task.setTaskName(jsonObject.getString(stdName.TASKNAME));
        task.setPoints(jsonObject.getInt(stdName.POINTS));
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, taskService.modify(task));
        JsonHelper.jsonToResponse(response, ret);
    }


    /**
     * 查看某个用户的所有任务
     *
     * @param request  http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/myTasks")
    public void checkMyTask(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("mytask");
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(stdName.USERNAME);
        String tag = jsonObject.getString(stdName.TAG);
        Integer status = jsonObject.getInt(stdName.STATUS);
        Integer userRole = jsonObject.getInt(stdName.USERROLE);
        JSONObject ret = new JSONObject();

        List<TaskEntity> tasks = taskService.checkMyTask(username, status, userRole, tag);
        JSONArray taskArray = new JSONArray();
        for (TaskEntity task : tasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put(stdName.TASKID, task.getId());
            taskObject.put(stdName.TASKNAME, task.getTaskName());
            taskObject.put(stdName.DESCRIPTION, task.getDescription());
            taskObject.put(stdName.IMGNUM, task.getImageNum());
            taskObject.put(stdName.SPONSORNAME, task.getSponsor().getUsername());
            JSONArray tags = new JSONArray();
            List<TagEntity> tagEntities = task.getTagEntityList();
            for (TagEntity tagEntity : tagEntities) {
                tags.put(tagEntity.getTag());
            }
            taskObject.put(stdName.TAG, tags);
            taskObject.put(stdName.TOTALPROGRESS, new Double(task.getCompletedNumber() / task.getExpectedNumber()));
            taskObject.put(stdName.STARTDATE, DateHelper.convertDateToString(task.getStartDate()));
            taskObject.put(stdName.ENDDATE, DateHelper.convertDateToString(task.getEndDate()));
            if (userRole.equals(4)) {
                Double progress = -1.0;
                List<WorkersForTheTaskEntity> workersForTheTaskEntities = task.getWorkersForTheTaskEntityList();
                for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
                    if (workersForTheTaskEntity.getWorker().getUsername().equals(username)) {
                        progress = ((double) workersForTheTaskEntity.getCompletedNum() / (double) task.getImageNum());
                    }
                }
                if (progress != -1.0) {
                    taskObject.put(stdName.PROGRESS, progress);
                    taskArray.put(taskObject);
                }
            } else {
                taskArray.put(taskObject);
            }
        }
        ret.put(stdName.TASKS, taskArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 查看当前所有任务以及搜索任务
     *
     * @param request http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allTasks")
    public List<TaskEntity> search(HttpServletRequest request) {
        System.out.println("all");
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Integer userRole = jsonObject.getInt(stdName.USERROLE);
        String tag = jsonObject.getString(stdName.TAG);
        Integer status = jsonObject.getInt(stdName.STATUS);
        return taskService.search(userRole, tag, status);

    }

    /**
     * 结束任务
     *
     * @param request 编号
     * @return 是否成功结束该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/endTask")
    public void endTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, taskService.endTask(taskID));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 删除任务
     *
     * @param request id
     * @return 是否成功删除该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/deleteTask")
    public void deleteTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, taskService.deleteTask(taskID));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 完成任务,增加对应积分
     *
     * @param request 任务ID 工人用户名
     * @return 是否成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/completeTask")
    public void completeTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        String username = map.get(stdName.USERNAME)[0];
        UserEntity userInfo = userService.findByUsername(username);
        userInfo.setPoints(userInfo.getPoints() + taskEntity.getPoints());
        userInfo.setBonus(userInfo.getBonus() + taskEntity.getPoints());
        userInfo.setLev((int) (Math.log(userInfo.getBonus() + 1) / Math.log(10)) + 1);
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, taskService.completeTask(taskID, username) && userService.modify(userInfo));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 放弃任务
     *
     * @param request 任务ID 工人用户名
     * @return 是否成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/abortTask")
    public void abortTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        String username = map.get(stdName.USERNAME)[0];
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, taskService.abortTask(taskID, username));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 接受任务
     *
     * @param request 任务ID 工人用户名
     * @return 是否接受成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/acceptTask")
    public void acceptTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        String username = map.get(stdName.USERNAME)[0];
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, taskService.acceptTask(taskID, username));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 举报任务
     *
     * @param
     * @return boolean 成功与否
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportTask")
    public void reportTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID));
        UserEntity respondent = userService.findByUsername(jsonObject.getString(stdName.RESPONDENT));
        UserEntity reporter = userService.findByUsername(jsonObject.getString(stdName.REPORTER));
        ReportmessageEntity reportmessageEntity = new ReportmessageEntity();
        JSONObject ret = new JSONObject();
        reportmessageEntity.setReportTime((java.sql.Date) new Date());
        reportmessageEntity.setDescription(jsonObject.getString(stdName.DESCRIPTION));
        reportmessageEntity.setIsDealt((byte) 0);
        reportmessageEntity.setReporter(reporter);
        reportmessageEntity.setRespondent(respondent);
        reportmessageEntity.setTask(taskEntity);
        reportmessageEntity.setType(0);
        ret.put(stdName.MES, reportService.add(reportmessageEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 举报工人
     *
     * @param
     * @return boolean 成功与否
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportWorker")
    public void reportWorker(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID));
        UserEntity respondent = userService.findByUsername(jsonObject.getString(stdName.RESPONDENT));
        UserEntity reporter = userService.findByUsername(jsonObject.getString(stdName.REPORTER));
        ReportmessageEntity reportmessageEntity = new ReportmessageEntity();
        JSONObject ret = new JSONObject();
        reportmessageEntity.setReportTime((java.sql.Date) new Date());
        reportmessageEntity.setDescription(jsonObject.getString(stdName.DESCRIPTION));
        reportmessageEntity.setIsDealt((byte) 0);
        reportmessageEntity.setReporter(reporter);
        reportmessageEntity.setRespondent(respondent);
        reportmessageEntity.setTask(taskEntity);
        reportmessageEntity.setType(1);
        ret.put(stdName.MES, reportService.add(reportmessageEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 管理员查看用户被举报的所有信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkWorkerReport")
    public List<ReportmessageEntity> checkWorkerReport() {
        return reportService.checkWorkerReport();
    }

    /**
     * 管理员查看任务被举报的所有信息
     *
     * @param
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskReport")
    public List<ReportmessageEntity> checkTaskReport() {
        return reportService.checkTaskReport();
    }

    /**
     * 所有人员查看任务细节
     * 参数是taskID和人员角色
     *
     * @param response http
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskDetail")
    public void checkTaskDetail(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        JSONObject ret = new JSONObject(taskEntity);
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        ret.put(stdName.ACCEPTNUM, workersForTheTaskEntities.size());
        Integer totalTagNum = 0;
        JSONArray userInfos = new JSONArray();
        for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
            totalTagNum += workersForTheTaskEntity.getMarkedNum();
            JSONObject jsonObject = new JSONObject();

            UserEntity userEntity = workersForTheTaskEntity.getWorker();
            jsonObject.put(stdName.USERNAME, userEntity.getUsername());
            jsonObject.put(stdName.LEVEL, userEntity.getLev());
            jsonObject.put(stdName.COMPLETEDNUMBER, workersForTheTaskEntity.getCompletedNum());
            userInfos.put(jsonObject);
        }
        ret.put(stdName.TOTALTAGNUM, totalTagNum);
        ret.put(stdName.WORKERINFO, userInfos);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 管理员处理举报信息
     *
     * @param request  http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/dealReport")
    public void dealReport(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String reportTime = jsonObject.getString(stdName.REPORTTIME);
        String description = jsonObject.getString(stdName.DESCRIPTION);
        Integer type = jsonObject.getInt(stdName.TYPE);

        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, reportService.dealReport(reportTime, type, description));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看某用户某次任务的所有图片的url
     * @date: 22:58 2018/4/24
     */
    @RequestMapping(value = "/checkImages", method = RequestMethod.GET)
    public void checkImages(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
        for (ImageEntity imageEntity : imageEntities) {
            array.put(imageEntity.getUrl());
        }
        ret.put(stdName.IMGURLS, array);
        JsonHelper.jsonToResponse(response, ret);
    }

    @RequestMapping(value = "/checkWorkerProgress", method = RequestMethod.GET)
    public void checkWorkerProgress(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
            if (workersForTheTaskEntity.getWorker().getUsername().equals(userName)) {
                ret.put(stdName.PROGRESS, workersForTheTaskEntity.getCompletedNum() / taskEntity.getImageNum());
                JsonHelper.jsonToResponse(response, ret);
                return;
            }
        }
        ret.put(stdName.MES, stdName.ERROR);
        JsonHelper.jsonToResponse(response, ret);
    }

    private void addTag(JSONArray tagArray, List<TagEntity> tagEntities) {
        for (Object o : tagArray) {
            String str = (String) o;
            TagEntity tagEntity = tagService.findByTag(str);
            if (tagEntity != null)
                tagEntities.add(tagEntity);
        }
    }
}
