package cn.joker.controller.taskcontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.stdName;
import cn.joker.sevice.ReportService;
import cn.joker.sevice.TagService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
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
        task.setType(jsonObject.getInt(stdName.TYPE));
        UserEntity userEntity = userService.findByUsername(jsonObject.getString(stdName.SPONSORNAME));
        task.setSponsor(userEntity);



        JSONArray tagArray = jsonObject.getJSONArray(stdName.TAG);
        List<TagEntity> tagEntities = new ArrayList<>();

        addTag(tagArray, tagEntities);
        task.setTagEntityList(tagEntities);
        task.setImageNum(0);
        task.setTaskName(jsonObject.getString(stdName.TASKNAME));
        task.setWorkerLevel(jsonObject.getInt(stdName.WORKERLEVEL));
        task.setState(1);
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
        task.setTaskName(jsonObject.getString(stdName.TASKNAME));
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
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(stdName.USERNAME);
        String tag = jsonObject.getString(stdName.TAG);
        Integer status = jsonObject.getInt(stdName.STATUS);
        Integer userRole = jsonObject.getInt(stdName.USERROLE);
        JSONObject ret = new JSONObject();

        List<TaskEntity> tasks = taskService.checkMyTask(username, status, userRole, tag);
        JSONArray taskArray = new JSONArray();
        getTasks(tasks, taskArray);
        ret.put(stdName.TASKS, taskArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 查看当前所有任务以及搜索任务
     *
     * @param request http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allTasks")
    public void search(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Integer userRole = jsonObject.getInt(stdName.USERROLE);
        String tag = jsonObject.getString(stdName.TAG);
        Integer status = jsonObject.getInt(stdName.STATUS);
        List<TaskEntity> tasks = taskService.search(userRole, tag, status);
        JSONObject ret = new JSONObject();
        JSONArray taskArray = new JSONArray();
        getTasks(tasks, taskArray);
        ret.put(stdName.TASKS, taskArray);
        JsonHelper.jsonToResponse(response, ret);

    }

    private void getTasks(List<TaskEntity> tasks, JSONArray taskArray) {
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
            taskObject.put(stdName.STARTDATE, DateHelper.convertDateToString(task.getStartDate()));
            taskObject.put(stdName.ENDDATE, DateHelper.convertDateToString(task.getEndDate()));
            taskArray.put(taskObject);
        }
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
        reportmessageEntity.setReportTime(new java.sql.Date(System.currentTimeMillis()));
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
     * @param request
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
        TaskEntity task = (TaskEntity) taskService.findByID(taskID);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(stdName.TASKID, task.getId());

        jsonObject.put(stdName.SPONSORNAME, task.getSponsor().getUsername());
        jsonObject.put(stdName.TASKNAME, task.getTaskName());
        jsonObject.put(stdName.DESCRIPTION, task.getDescription());
        JSONArray tags = new JSONArray();
        List<TagEntity> tagEntities = task.getTagEntityList();
        for (TagEntity tagEntity : tagEntities) {
            tags.put(tagEntity.getTag());
        }
        jsonObject.put(stdName.TAG, tags);
        jsonObject.put(stdName.LEVEL, task.getWorkerLevel());

        jsonObject.put(stdName.STARTDATE, DateHelper.convertDateToString(task.getStartDate()));
        jsonObject.put(stdName.ENDDATE, DateHelper.convertDateToString(task.getEndDate()));
        jsonObject.put(stdName.IMGNUM, task.getImageNum());

        Integer totalTagNum = 0;
        JSONArray userInfos = new JSONArray();
        jsonObject.put(stdName.TOTALTAGNUM, totalTagNum);
        jsonObject.put(stdName.AVERAGETAGNUM, totalTagNum / task.getImageNum());
        jsonObject.put(stdName.WORKERINFO, userInfos);
        JsonHelper.jsonToResponse(response, jsonObject);
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

    /**
     * @author:pis
     * @description: 发起者下载任务标注的数据集
     * @date: 14:30 2018/6/5
     */
    @RequestMapping(value = "getDataSet", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getDataSet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(stdName.TASKID)[0]);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        return taskService.getDataSet(taskEntity);
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
