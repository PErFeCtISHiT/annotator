package cn.joker.controller.taskcontrollers;

import cn.joker.dao.TaskDao;
import cn.joker.entity.ReportMessage;
import cn.joker.entity.Task;
import cn.joker.entity.UserInfo;
import cn.joker.sevice.ImgMarkService;
import cn.joker.sevice.ReportService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserInfoService;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private UserInfoService userInfoService;
    private String globalTaskID = "taskID";
    private String globalSponsorName = "sponsorName";
    private String globalTaskName = "taskName";
    private String globalDescription = "description";
    private String globalWorkerLevel = "workerLevel";
    private String globalTag = "tag";
    private String globalPoints = "points";
    private String globalExpectedNumber = "expectedNumber";
    private String globalCompletedNumber = "completedNumber";
    private String globalStartDate = "startDate";
    private String globalEndDate = "endDate";
    private String globalMes = "mes";
    private String globalUsername = "username";
    private String globalUserRole = "userRole";
    private String globalStatus = "status";
    private String globalTasks = "tasks";
    private String globalImgNum = "imgNum";
    private String globalReportList = "reportList";

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
        Task task = new Task();

        task.setDescription(jsonObject.getString(globalDescription));
        String endDate = jsonObject.getString(globalEndDate);
        endDate += " 00:00:00";
        task.setEndDate(DateHelper.convertStringToDate(endDate));
        task.setExpectedNumber(jsonObject.getInt(globalExpectedNumber));
        task.setPoints(jsonObject.getInt(globalPoints));
        task.setSponsorName(jsonObject.getString(globalSponsorName));


        JSONArray tagArray = jsonObject.getJSONArray(globalTag);
        List list = tagArray.toList();
        String[] tags = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            tags[i] = (String) list.toArray()[i];
        }
        task.setTag(tags);
        task.setImageNum(0);
        task.setTaskName(jsonObject.getString(globalTaskName));
        task.setWorkerLevel(jsonObject.getInt(globalWorkerLevel));
        JSONObject ret = new JSONObject();

        ret.put(globalMes, taskService.releaseTask(task));
        ret.put("taskID",12);
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
        Task task = new Task();
        task.setDescription(jsonObject.getString(globalDescription));
        task.setEndDate(DateHelper.convertStringToDate(jsonObject.getString(globalEndDate)));
        JSONArray tagArray = jsonObject.getJSONArray(globalTag);
        List list = tagArray.toList();
        String[] tags = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            tags[i] = (String) list.toArray()[i];
        }
        task.setTag(tags);
        task.setExpectedNumber(jsonObject.getInt(globalExpectedNumber));
        task.setTaskName(jsonObject.getString(globalTaskName));
        task.setPoints(jsonObject.getInt(globalPoints));
        task.setTaskID(jsonObject.getInt(globalTaskID));
        JSONObject ret = new JSONObject();
        ret.put(globalMes, taskService.modifyTask(task));
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

        Integer userRole = jsonObject.getInt(globalUserRole);
        JSONObject ret = new JSONObject();

        List<Task> tasks = taskService.checkMyTask(username, 0, userRole);
        JSONArray taskArray = new JSONArray();
        for (Task task : tasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put(globalTaskID, task.getTaskID());
            taskObject.put(globalTaskName, task.getTaskName());
            taskObject.put(globalDescription, task.getDescription());
            taskObject.put(globalImgNum,task.getImageNum());
            taskObject.put(globalSponsorName, task.getSponsorName());
            if (userRole.equals(3))
                taskObject.put("progress", taskService.checkTaskProgress(task.getTaskID(), username));
            taskObject.put("totalProgress", new Double(task.getCompletedNumber() / task.getExpectedNumber()));
            taskObject.put(globalStartDate, DateHelper.convertDateToString(task.getStartDate()));
            taskObject.put(globalEndDate, DateHelper.convertDateToString(task.getEndDate()));
            taskArray.put(taskObject);
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
        System.out.println(ret.toString());
        ret.put("acceptNum", ret.getJSONArray("userName").length());
        ret.put("totalProgress", (double) ret.getInt("completedNumber") / (double) ret.getInt("expectedNumber"));
        JSONArray users = ret.getJSONArray("userName");
        JSONArray newUsers = new JSONArray();
        for (Object o : users) {
            String string = (String) o;
            newUsers.put(string.split("-")[0]);
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
}
