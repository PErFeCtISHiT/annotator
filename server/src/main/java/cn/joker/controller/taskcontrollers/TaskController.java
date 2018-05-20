package cn.joker.controller.taskcontrollers;

import cn.joker.dao.TaskDao;
import cn.joker.entity.TagEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
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
import org.springframework.web.bind.annotation.*;

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

        task.setDescription(jsonObject.getString(stdName.DESCRIPTION));
        String endDate = jsonObject.getString(stdName.ENDDATE);
        String startDate = jsonObject.getString(stdName.STARTDATE);
        task.setStartDate(DateHelper.convertStringToDate(startDate));
        task.setEndDate(DateHelper.convertStringToDate(endDate));
        task.setExpectedNumber(jsonObject.getInt(stdName.EXCEPTEDNUMBER));
        task.setPoints(jsonObject.getInt(stdName.POINTS));
        UserEntity userEntity = userService.findByUsername(stdName.SPONSORNAME);
        task.setSponsor(userEntity);
        userEntity.setPoints(userEntity.getPoints() - task.getPoints() * task.getExpectedNumber());

        JSONArray tagArray = jsonObject.getJSONArray(stdName.TAG);
        List<TagEntity> tagEntities = new ArrayList<>();

        addTag(tagArray, tagEntities);
        task.setTagEntityList(tagEntities);
        task.setImageNum(0);
        task.setTaskName(jsonObject.getString(stdName.TASKNAME));
        task.setWorkerLevel(jsonObject.getInt(stdName.WORKERLEVEL));
        JSONObject ret = new JSONObject();

        ret.put(stdName.MES, userService.modify(userEntity));
        ret.put(stdName.TASKID, taskService.add(task));
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
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(globalUsername);
        String tag = jsonObject.getString(globalTag);
        Integer status = jsonObject.getInt(globalStatus);
        Integer userRole = jsonObject.getInt(globalUserRole);
        JSONObject ret = new JSONObject();

        List<Task> tasks = taskService.checkMyTask(username, status, userRole, tag);
        JSONArray taskArray = new JSONArray();
        for (Task task : tasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put(globalTaskID, task.getTaskID());
            taskObject.put(globalTaskName, task.getTaskName());
            taskObject.put(globalDescription, task.getDescription());
            taskObject.put(globalImgNum, task.getImageNum());
            taskObject.put(globalSponsorName, task.getSponsorName());
            taskObject.put(globalTag, task.getTag());
            taskObject.put("totalProgress", new Double(task.getCompletedNumber() / task.getExpectedNumber()));
            taskObject.put(globalStartDate, DateHelper.convertDateToString(task.getStartDate()));
            taskObject.put(globalEndDate, DateHelper.convertDateToString(task.getEndDate()));
            if (userRole.equals(3)) {
                Double progress = taskService.checkTaskProgress(task.getTaskID(), username);
                if (progress != -1.0) {
                    taskObject.put("progress", progress);
                    taskArray.put(taskObject);
                }
            } else {
                taskArray.put(taskObject);
            }
        }
        ret.put(globalTasks, taskArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 查看当前所有任务以及搜索任务
     *
     * @param request  http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allTasks")
    public void search(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Integer userRole = jsonObject.getInt(globalUserRole);
        String tag = jsonObject.getString(globalTag);
        Integer status = jsonObject.getInt(globalStatus);
        List<Task> tasks = taskService.search(userRole, tag, status);
        TaskDao taskDao = new TaskDao();
        JSONArray taskArray = new JSONArray();
        for (Task task : tasks) {
            JSONObject taskObj = taskDao.convertObjectToJsonObject(task);
            taskObj.put("totalProgress", new Double(task.getCompletedNumber() / task.getExpectedNumber()));
            taskArray.put(taskObj);
        }
        JSONObject ret = new JSONObject();
        ret.put(globalTasks, taskArray);
        JsonHelper.jsonToResponse(response, ret);

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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        JSONObject ret = new JSONObject();
        ret.put(globalMes, taskService.endTask(taskID));
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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        JSONObject ret = new JSONObject();
        ret.put(globalMes, taskService.deleteTask(taskID));
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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        JSONObject task = taskService.checkTaskDetail(taskID);
        String username = map.get(globalUsername)[0];
        UserInfo userInfo = userInfoService.findByUsername(username);
        userInfo.setPoints(userInfo.getPoints() + task.getInt(globalPoints));
        userInfo.setLevel(userInfo.getLevel() + task.getInt(globalPoints));
        JSONObject ret = new JSONObject();
        ret.put(globalMes, taskService.completeTask(taskID, username) && userInfoService.modifyUser(userInfo));
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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        String username = map.get(globalUsername)[0];
        JSONObject ret = new JSONObject();
        ret.put(globalMes, taskService.abortTask(taskID, username));
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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        String username = map.get(globalUsername)[0];
        JSONObject ret = new JSONObject();
        ret.put(globalMes, taskService.acceptTask(taskID, username));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 举报任务
     *
     * @param reportMessage 举报信息
     * @return boolean 成功与否
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportTask")
    public void reportTask(@RequestBody ReportMessage reportMessage, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        reportMessage.setReportTime(DateHelper.convertDateToString(new Date()));
        ret.put(globalMes, reportService.reportTask(reportMessage));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 举报工人
     *
     * @param reportMessage 举报信息
     * @return boolean 成功与否
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportWorker")
    public void reportWorker(@RequestBody ReportMessage reportMessage, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        reportMessage.setReportTime(DateHelper.convertDateToString(new Date()));
        ret.put(globalMes, reportService.reportWorker(reportMessage));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 管理员查看用户被举报的所有信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkWorkerReport")
    public void checkWorkerReport(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        ret.put(globalReportList, reportService.checkWorkerReport());
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 管理员查看任务被举报的所有信息
     *
     * @param response http
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskReport")
    public void checkTaskReport(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        ret.put(globalReportList, reportService.checkTaskReport());
        JsonHelper.jsonToResponse(response, ret);
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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        JSONObject ret = taskService.checkTaskDetail(taskID);
        ret.put("acceptNum", ret.getJSONArray("userName").length());
        ret.put("totalProgress", (double) ret.getInt("completedNumber") / (double) ret.getInt("expectedNumber"));
        JSONArray users = ret.getJSONArray("userName");
        JSONArray newUsers = new JSONArray();
        JSONArray userInfos = new JSONArray();
        for (Object o : users) {
            String string = (String) o;
            newUsers.put(string.split("-")[0]);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(globalUsername, string.split("-")[0]);
            UserInfo userInfo = userInfoService.findByUsername(string.split("-")[0]);
            jsonObject.put("level", userInfo.getRealLevel());
            jsonObject.put(globalCompletedNumber, Integer.valueOf(string.split("-")[1]));
            userInfos.put(jsonObject);
        }
        List imgURLs = taskService.findImgURLByID(String.valueOf(taskID));
        Integer totalTagNum = 0;
        JSONArray jsonArray = new JSONArray();
        for (Object o1 : imgURLs) {
            JSONObject object = new JSONObject();
            String imgURL = (String) o1;
            String imgName = imgURL.substring(imgURL.lastIndexOf('/') + 1, imgURL.lastIndexOf('.'));
            object.put(imgName, taskService.findMarkNumByImgNameAndUserAndID(taskID, imgName, newUsers));
            totalTagNum += taskService.findMarkNumByImgNameAndUserAndID(taskID, imgName, newUsers);
            jsonArray.put(object);
        }
        ret.put("totalTagNum", totalTagNum);
        ret.put("averageTagNum", totalTagNum / imgURLs.size());
        ret.put("totalImgTagNum", jsonArray);
        ret.put("workerInfo", userInfos);
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
        String reportTime = jsonObject.getString("reportTime");
        String description = jsonObject.getString("description");
        Integer type = jsonObject.getInt("Type");

        JSONObject ret = new JSONObject();
        ret.put(globalMes, reportService.dealReport(reportTime, type, description));
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
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        List list = taskService.findImgURLByID(String.valueOf(taskID));
        for (Object o : list) {
            String imgURL = (String) o;
            array.put(imgURL);
        }
        ret.put("imgURLs", array);
        JsonHelper.jsonToResponse(response, ret);
    }

    @RequestMapping(value = "/checkWorkerProgress", method = RequestMethod.GET)
    public void checkWorkerProgress(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        Integer taskID = Integer.valueOf(map.get(globalTaskID)[0]);
        JSONObject ret = new JSONObject();
        ret.put("progress", taskService.checkTaskProgress(taskID, userName));
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
