package cn.joker.controller.taskcontrollers;

import cn.joker.entity.ReportMessage;
import cn.joker.entity.Task;
import cn.joker.sevice.TaskService;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;
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
    private String globalStatus = "status";


    /**
     * 发起任务
     * @param request http
     * @return 任务是否发布成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/releaseTask")
    public void releaseTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Task task = new Task();
        task.setTaskID(taskService.generateID());
        task.setCompletedNumber(0);
        task.setDescription(jsonObject.getString(globalDescription));
        task.setEndDate(DateHelper.convertStringtoDate(jsonObject.getString(globalEndDate)));
        task.setExpectedNumber(jsonObject.getInt(globalExpectedNumber));
        task.setPoints(jsonObject.getInt(globalPoints));
        task.setSponsorName(jsonObject.getString(globalSponsorName));
        task.setStartDate(DateHelper.convertStringtoDate(jsonObject.getString(globalStartDate)));
        String[] tags = new String[]{};
        JSONArray tagArray = jsonObject.getJSONArray(globalTag);
        for(int i = 0;i < tagArray.length();i++){
            tags[i] = (String) tagArray.get(i);
        }
        task.setTag(tags);
        task.setStatus(1);
        task.setTaskName(jsonObject.getString(globalTaskName));
        task.setUserName(null);
        task.setWorkerLevel(jsonObject.getInt(globalWorkerLevel));
        JSONObject ret = new JSONObject();

        ret.put(globalMes, taskService.releaseTask(task));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 修改任务
     * @param task
     * @return 任务是否发布成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modifyTask")
    public void modifyTask(Task task, HttpServletRequest response) {
        //TODO
        return;
    }

    /**
     * 查看某个用户的所有任务
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/myTask")
    public void checkMyTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(globalUsername);

        Integer status = jsonObject.getInt(globalStatus);
        Integer userRole = jsonObject.getInt("userRole");
        JSONObject ret = new JSONObject();

        List<Task> tasks = taskService.checkMyTask(username,status,userRole);
        JSONArray taskArray = new JSONArray();
        for(Task task : tasks){
            JSONObject taskObject = new JSONObject();
            taskObject.put(globalTaskID,task.getTaskID());
            taskObject.put(globalTaskName,task.getTaskName());
            taskObject.put(globalDescription,task.getDescription());
            taskObject.put(globalSponsorName,task.getSponsorName());
            if(userRole.equals(3))
            taskObject.put("progress",taskService.checkTaskProgress(task.getTaskID(),username));
            taskObject.put("totalProgress", new Double(task.getCompletedNumber() / task.getExpectedNumber()));
            taskObject.put(globalStartDate,DateHelper.convertDateToString(task.getStartDate()));
            taskObject.put(globalEndDate,DateHelper.convertDateToString(task.getEndDate()));
            taskArray.put(taskObject);
        }
        ret.put("tasks",taskArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 查看当前所有任务以及搜索任务
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allTasks")
    public void search(HttpServletRequest request, HttpServletRequest response){
        //TODO
    }

    /**
     * 结束任务
     * @param taskID
     * @return 是否成功结束该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/endTask")
    public void endTask(String taskID, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 删除任务
     * @param taskID
     * @return 是否成功删除该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/deleteTask")
    public void deleteTask(String taskID, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 完成任务
     * @param taskID 任务ID
     * @param workerName 工人用户名
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/completeTask")
    public void completeTask(Integer taskID, String workerName, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 放弃任务
     * @param taskID 任务ID
     * @param workerName 工人用户名
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/abortTask")
    public void abortTask(Integer taskID, String workerName, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 接受任务
     * @param taskID 任务ID
     * @param workerName 工人用户名
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/acceptTask")
    public void acceptTask(Integer taskID, String workerName, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 举报任务
     * @param reportMessage 举报信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportTask")
    public void reportTask(ReportMessage reportMessage, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 举报工人
     * @param reportMessage 举报信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportWorker")
    public void reportWorker(ReportMessage reportMessage, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 管理员查看用户被举报的所有信息
     * @param response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkWorkerReport")
    public void checkWorkerReport(HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 管理员查看任务被举报的所有信息
     * @param response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskReport")
    public void checkTaskReport(HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 所有人员查看任务细节
     * 参数是taskID和人员角色
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/checkTaskDetail")
    public void checkTaskDetail(HttpServletRequest request, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 管理员处理举报信息
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/dealReport")
    public void dealReport(HttpServletRequest request, HttpServletRequest response){
        //todo
    }

}
