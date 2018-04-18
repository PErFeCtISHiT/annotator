package cn.joker.dao;

import cn.joker.entity.Task;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private String globalJson = "json/task.json";

    private String globalTaskID = "taskID";//任务ID，任务识别标识
    private String globalSponsorName = "sponsorName";//发起者名字
    private String globalTaskName = "taskName";//任务名字
    private String globalDescription = "description";//任务描述
    private String globalTag = "tag";//图片标签，便于搜索任务
    private String globalWorkerLevel = "workerLevel";//用户等级要求，可选项
    private String globalPoints = "points";//用户完成任务的积分奖励
    private String globalExpectedNumber = "expectedNumber";//预计完成数量要求，满足该要求自动关闭任务
    private String globalCompletedNumebr = "completedNumber";//目前已完成数量
    private String globalStartDate = "startDate";//开始时间
    private String globalEndDate = "endDate";//结束时间
    private String globalUserName = "userName";//标注用户的用户名

    private String globalJsonTasks = "{\"tasks\":[";
    private String globalTasks = "tasks";

    public boolean releaseTask(Task task){
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            JsonObject object = (JsonObject) o;
            newJson.append(object.toString());
            newJson.append(",");
        }

        //先得到taskID 这边要生成 TODO
        task.setTaskID(0);

        //接收该任务的用户列表初始化为空
        task.setUserName(new ArrayList<String>());

        JSONObject jsonObject = convertObjectToJsonObject(task);

        newJson.append(jsonObject.toString());
        newJson.append("]}");

        return this.updateJson(newJson);
    }

    public boolean modifyTask(Task task){
        ArrayList<Task> allTasks = (ArrayList)search(1, null, 0);

        Task t = getTask(task.getTaskID(), allTasks);

        return false;
    }

    public List<Task> checkMyTask(String userName, Integer status, Integer userRole){
        return null;
    }

    /**
     * 1， null， 0就能获得当前所有的任务了
     * @param userRole 用户权限（Integer）1 管理员 2 发起者 3 工人
     * @param tag 图片标签（String）只支持一个tag搜索
     * @param status 任务状态（Integer）0 所有 1 正在进行 2 已结束
     * @return
     */
    public List<Task> search(int userRole, String tag, Integer status){
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        ArrayList<Task> tasks = convertJsonArrayToArrayList(taskArray);

        //对结果进行过滤 TODO
        switch (userRole){

        }

        switch (tag){

        }

        switch (status){

        }

        return tasks;
    }

    //要搜索客户那边的任务，全部删除
    public boolean endTask(Integer taskID){
        return false;
    }

    public boolean deleteTask(Integer  taskID){
        return false;
    }

    public boolean completeTask(Integer taskID, String workerName){
        return false;
    }

    public boolean abortTask(Integer taskID, String workerName){
        return false;
    }

    public boolean acceptTask(Integer taskID, String workerName){
        return false;
    }

    //目前还不确定，好像这个返回值有点多
    public Task checkTaskDetail(Integer taskID, Integer userRole){
        return null;
    }

    //更新json文件
    private boolean updateJson(StringBuilder newJson) {
        return JsonHelper.modifyJson(newJson, globalJson);
    }

    //得到目前的任务列表，数组形式
    private ArrayList<Task> convertJsonArrayToArrayList(JsonArray taskArray) {
        ArrayList<Task> tasks = new ArrayList<>();

        for (Object o : taskArray) {
            Task task = new Task();
            JsonObject object = (JsonObject) o;
            task.setTaskID(Integer.valueOf(JsonHelper.format(object.get(globalTaskID).toString())));
            task.setSponsorName(JsonHelper.format(object.get(globalSponsorName).toString()));
            task.setTaskName(JsonHelper.format(object.get(globalTaskName).toString()));
            task.setDescription(JsonHelper.format(object.get(globalDescription).toString()));
            //数组转换 todo
            //task.setTag(JsonHelper.format(object.get(globalTag).toString()));
            //task.setUserName();

            task.setWorkerLevel(Integer.valueOf(JsonHelper.format(object.get(globalWorkerLevel).toString())));
            task.setExpectedNumber(Integer.valueOf(JsonHelper.format(object.get(globalExpectedNumber).toString())));
            task.setCompletedNumber(Integer.valueOf(JsonHelper.format(object.get(globalCompletedNumebr).toString())));
            task.setPoints(Integer.valueOf(JsonHelper.format(object.get(globalPoints).toString())));

            task.setStartDate(DateHelper.convertStringtoDate(JsonHelper.format(object.get(globalStartDate).toString())));
            task.setEndDate(DateHelper.convertStringtoDate(JsonHelper.format(object.get(globalEndDate).toString())));

            tasks.add(task);
        }

        return tasks;
    }

    private JSONObject convertObjectToJsonObject(Task task){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(globalTaskID, task.getTaskID());

        jsonObject.put(globalSponsorName, task.getSponsorName());
        jsonObject.put(globalTaskName, task.getTaskName());
        jsonObject.put(globalDescription, task.getDescription());
        jsonObject.put(globalTag, task.getTag());
        jsonObject.put(globalWorkerLevel, task.getWorkerLevel());
        jsonObject.put(globalPoints, task.getPoints());
        jsonObject.put(globalExpectedNumber, task.getExpectedNumber());

        jsonObject.put(globalCompletedNumebr, 0);

        //时间格式要修改
        jsonObject.put(globalStartDate, DateHelper.convertDateToString(task.getStartDate()));
        jsonObject.put(globalEndDate, DateHelper.convertDateToString(task.getEndDate()));

        //接收该任务的用户列表 ArrayList转换 todo
        //jsonObject.put(globalUserName, );

        return jsonObject;
    }

    private Task getTask(Integer taskID, ArrayList<Task> allTasks){
        for(Task t : allTasks){
            if(t.getTaskID().equals(taskID)){
                return t;
            }
        }
        return null;
    }
}
