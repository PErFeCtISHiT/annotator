package cn.joker.dao;

import cn.joker.entity.Task;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
    private String globalCompletedNumber = "completedNumber";//目前已完成数量
    private String globalStartDate = "startDate";//开始时间
    private String globalEndDate = "endDate";//结束时间
    private String globalImgNum = "imgNum"; //任务图片总数
    private String globalUserName = "userName";//标注用户的用户名

    private String globalJsonTasks = "{\"tasks\":[";
    private String globalTasks = "tasks";

    public Integer releaseTask(Task task) {
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

        //先得到taskID 这边要生成
        if (taskArray.size() != 0) {
            task.setTaskID(Integer.parseInt(taskArray.get(taskArray.size() - 1).getAsJsonObject().get(globalTaskID).toString()) + 1);
        } else
            task.setTaskID(1);

        //接收该任务的用户列表初始化为空
        task.setUserName(new ArrayList<>());
        //目前完成量为0
        task.setCompletedNumber(0);
        JSONObject jsonObject = convertObjectToJsonObject(task);

        newJson.append(jsonObject.toString());
        newJson.append("]}");
        this.updateJson(newJson);
        return task.getTaskID();
    }

    public boolean modifyTask(Task task) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        boolean existTask = false;

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.get("taskID").toString().equals(task.getTaskID().toString())) {
                existTask = true;

                jsonObject.put(globalTaskName, task.getTaskName());
                jsonObject.put(globalDescription, task.getDescription());
                jsonObject.put(globalTag, task.getTag());
                jsonObject.put(globalPoints, task.getPoints());
                jsonObject.put(globalExpectedNumber, task.getExpectedNumber());
                if(task.getImageNum() != null)
                    jsonObject.put(globalImgNum,task.getImageNum());
                //时间格式要修改
                jsonObject.put(globalEndDate, DateHelper.convertDateToString(task.getEndDate()));
            }

            newJson.append(jsonObject.toString());
            newJson.append(",");
        }

        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }

        newJson.append("]}");

        return existTask && this.updateJson(newJson);
    }

    /**
     * @param userName 用户名
     * @param status   status 任务状态（Integer）0 所有 1 正在进行 2 已结束
     * @param userRole 用户权限（Integer）1 管理员 2 发起者 3 工人
     * @param tag 标签
     * @return 任务列表
     */
    public List<Task> checkMyTask(String userName, Integer status, Integer userRole, String tag) {
        ArrayList<Task> tasks = getAllTasks();

        tasks = searchByStatus(tasks, status);

        ArrayList<Task> result = new ArrayList<>();
        if (userRole.equals(2)) {
            //查看个人发布的任务
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getSponsorName().equals(userName)) {
                    result.add(tasks.get(i));
                }
            }
        } else if (userRole.equals(3)) {
            //查看已接收任务
            for (int i = 0; i < tasks.size(); i++) {
                for (int j = 0; j < tasks.get(i).getUserName().size(); j++) {
                    if (userName.equals(tasks.get(i).getUserName().get(j).split("-")[0])) {
                        result.add(tasks.get(i));
                    }
                }
            }
        }

        result = searchByTag(result, tag);

        return result;
    }

    /**
     * 1， null， 0就能获得当前所有的任务了
     *
     * @param userRole 用户权限（Integer）1 管理员 2 发起者 3 工人
     * @param tag      图片标签（String）只支持一个tag搜索
     * @param status   任务状态（Integer）0 所有 1 正在进行 2 已结束
     * @return 满足条件的list
     */
    public List<Task> search(Integer userRole, String tag, Integer status) {
        ArrayList<Task> tasks = getAllTasks();

        tasks = searchByStatus(tasks, status);

        tasks = searchByTag(tasks, tag);

        return tasks;
    }

    //结束任务
    public boolean endTask(Integer taskID) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = (JsonObject) o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.getInt(globalTaskID) == taskID) {
                jsonObject.put(globalEndDate, DateHelper.convertDateToString(new Date()));
                jsonObject.put(globalCompletedNumber,jsonObject.getInt(globalExpectedNumber));
            }

            newJson.append(jsonObject.toString());
            newJson.append(",");
        }
        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }

        newJson.append("]}");

        return this.updateJson(newJson);
    }

    //删除任务
    public boolean deleteTask(Integer taskID) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = (JsonObject) o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.getInt(globalTaskID) != taskID) {
                newJson.append(jsonObject.toString());
                newJson.append(",");
            }
        }
        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");

        return this.updateJson(newJson);
    }

    //完成任务
    public boolean completeTask(Integer taskID, String workerName) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = (JsonObject) o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.getInt(globalTaskID) == taskID) {
                //工人标注数量修改
                ArrayList<String> userArray = (ArrayList) jsonObject.getJSONArray(globalUserName).toList();
                for (int i = 0; i < userArray.size(); i++) {
                    if (workerName.equals((userArray.get(i).split("-")[0]))) {
                        //如果工人之前已经完成了，那完成数量就不能加了
                        if (!userArray.get(i).split("-")[1].equals(jsonObject.get(globalImgNum).toString()))
                            //完成数量加1
                            jsonObject.put(globalCompletedNumber, Integer.valueOf(jsonObject.get(globalCompletedNumber).toString()) + 1);

                        userArray.set(i, userArray.get(i).split("-")[0] + "-" + jsonObject.get(globalImgNum).toString());
                        jsonObject.put(globalUserName, userArray);
                    }
                }

                //判断是否结束任务
                if (jsonObject.get(globalCompletedNumber).toString().equals(jsonObject.get(globalExpectedNumber).toString())) {
                    //时间格式要修改
                    jsonObject.put(globalEndDate, DateHelper.convertDateToString(new Date()));
                }
            }

            newJson.append(jsonObject.toString());
            newJson.append(",");
        }
        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");

        return this.updateJson(newJson);
    }

    /**
     * 主要是用户那边和任务属性要修改
     *
     * @param taskID     任务ID
     * @param workerName 工人用户名
     * @return 成功与否
     */
    public boolean abortTask(Integer taskID, String workerName) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = (JsonObject) o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.getInt(globalTaskID) == taskID) {
                //工人标注
                ArrayList<String> userArray = (ArrayList) jsonObject.getJSONArray(globalUserName).toList();
                for (int i = 0; i < userArray.size(); i++) {
                    if (workerName.equals((userArray.get(i).split("-")[0]))) {
                        //如果该工人已经完成该任务，数据库不删除
                        userArray.remove(i);
                        jsonObject.put(globalUserName, userArray);
                    }
                }
            }

            newJson.append(jsonObject.toString());
            newJson.append(",");
        }
        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");

        return this.updateJson(newJson);
    }

    /**
     * 主要是用户那边和任务属性要修改
     *
     * @param taskID     任务ID
     * @param workerName 工人用户名
     * @return 成功与否
     */
    public boolean acceptTask(Integer taskID, String workerName) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.getInt(globalTaskID) == taskID) {
                //工人标注数量修改
                ArrayList<String> userArray = (ArrayList) jsonObject.getJSONArray(globalUserName).toList();
                for(String str : userArray){
                    if(str.split("-")[0].equals(workerName))
                        return false;
                }
                userArray.add(workerName + "-0");
                jsonObject.put(globalUserName, userArray);
            }

            newJson.append(jsonObject.toString());
            newJson.append(",");
        }
        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");

        return this.updateJson(newJson);
    }

    //目前还不确定，好像这个返回值有点多
    //算了，直接把整个task返回给前端吧
    //加些东西，再说再说
    public JSONObject checkTaskDetail(Integer taskID) {
        ArrayList<Task> tasks = getAllTasks();

        return convertObjectToJsonObject(getTask(taskID, tasks));
    }

    //更新json文件
    private boolean updateJson(StringBuilder newJson) {
        return JsonHelper.modifyJson(newJson, globalJson);
    }

    //得到目前的任务列表，数组形式
    public ArrayList<Task> getAllTasks() {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        ArrayList<Task> tasks = new ArrayList<>();
        for (Object o : taskArray) {
            Object o1 = o;
            JSONObject object = new JSONObject(o1.toString());
            Task task = new Task();
            task.setTaskID(object.getInt(globalTaskID));
            task.setSponsorName(object.get(globalSponsorName).toString());
            task.setTaskName(object.get(globalTaskName).toString());
            task.setDescription(object.get(globalDescription).toString());

            JSONArray tagArray = object.getJSONArray(globalTag);
            List tags = tagArray.toList();
            String[] tag = new String[tags.size()];

            for (int i = 0; i < tags.size(); i++) {
                tag[i] = (String) tags.get(i);
            }

            task.setTag(tag);
            task.setImageNum(object.getInt(globalImgNum));
            JSONArray userArray = object.getJSONArray(globalUserName);
            task.setUserName((ArrayList) userArray.toList());
            //task.setUserName();

            task.setWorkerLevel(object.getInt(globalWorkerLevel));
            task.setExpectedNumber(object.getInt(globalExpectedNumber));
            task.setCompletedNumber(object.getInt(globalCompletedNumber));
            task.setPoints(object.getInt(globalPoints));

            task.setStartDate(DateHelper.convertStringToDate(object.get(globalStartDate).toString()));
            task.setEndDate(DateHelper.convertStringToDate(object.get(globalEndDate).toString()));

            tasks.add(task);
        }

        return tasks;
    }

    public JSONObject convertObjectToJsonObject(Task task) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(globalTaskID, task.getTaskID());

        jsonObject.put(globalSponsorName, task.getSponsorName());
        jsonObject.put(globalTaskName, task.getTaskName());
        jsonObject.put(globalDescription, task.getDescription());
        jsonObject.put(globalTag, task.getTag());
        jsonObject.put(globalWorkerLevel, task.getWorkerLevel());
        jsonObject.put(globalPoints, task.getPoints());
        jsonObject.put(globalExpectedNumber, task.getExpectedNumber());

        jsonObject.put(globalCompletedNumber, task.getCompletedNumber());

        //时间格式要修改
        jsonObject.put(globalStartDate, DateHelper.convertDateToString(task.getStartDate()));
        jsonObject.put(globalEndDate, DateHelper.convertDateToString(task.getEndDate()));

        //接收该任务的用户列表 ArrayList转换

        jsonObject.put(globalUserName, new JSONArray(task.getUserName().toArray()));
        jsonObject.put(globalImgNum, task.getImageNum());

        return jsonObject;
    }

    public Task getTask(Integer taskID, ArrayList<Task> allTasks) {
        for (Task t : allTasks) {
            if (t.getTaskID().equals(taskID)) {
                return t;
            }
        }
        return null;
    }

    public Double checkTaskProgress(Integer taskID, String workerName) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        for (Object o : taskArray) {
            Object o1 = o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            Double totalNum = Double.parseDouble(jsonObject.get(globalImgNum).toString());
            if (jsonObject.get("taskID").toString().equals(taskID.toString())) {

                //工人标注数量
                ArrayList<String> userArray = (ArrayList) jsonObject.getJSONArray(globalUserName).toList();
                for (int i = 0; i < userArray.size(); i++) {
                    if (workerName.equals((userArray.get(i).split("-")[0]))) {
                        Integer markNum = Integer.parseInt(userArray.get(i).split("-")[1]);
                        return markNum / totalNum;
                    }
                }
            }
        }
        return 0.0;
    }

    /**
     * @param allTasks 任务列表
     * @param status   任务状态（Integer）0 所有 1 正在进行 2 已结束
     * @return 满足条件的任务列表
     */
    private ArrayList<Task> searchByStatus(ArrayList<Task> allTasks, Integer status) {
        ArrayList<Task> result = new ArrayList<>();
        switch (status) {
            case 0:
                return allTasks;
            case 1:
                for (int i = 0; i < allTasks.size(); i++) {
                    if (!isEnd(allTasks.get(i))) {
                        result.add(allTasks.get(i));
                    }
                }
                return result;
            case 2:
                for (int i = 0; i < allTasks.size(); i++) {
                    if (isEnd(allTasks.get(i))) {
                        result.add(allTasks.get(i));
                    }
                }
                return result;
        }

        return allTasks;
    }

    private ArrayList<Task> searchByTag(ArrayList<Task> allTasks, String tag) {
        ArrayList<Task> result = new ArrayList<>();

        if (tag != null && tag.length() > 0) {
            for (int i = 0; i < allTasks.size(); i++) {
                String[] tags = allTasks.get(i).getTag();
                for (String tag1 : tags) {
                    if (tag1.equals(tag))
                        result.add(allTasks.get(i));
                }
            }
            return result;
        }
        else{
            return allTasks;
        }

    }

    private boolean isEnd(Task t) {
        return t.getEndDate().before(new Date());
    }

    public ArrayList<String> findImgURLByID(String taskID) {
        String path = System.getProperty("user.dir") + "/annotator/task/" + taskID + "/images";
        File dir = new File(path);
        String files[] = dir.list();
        ArrayList<String> ret = new ArrayList<>();
        assert files != null;
        for (String file : files) {
            file = "task/" + taskID + "/images/" + file;
            ret.add(file);
        }
        return ret;
    }


    public Integer findMarkNumByImgNameAndUser(Integer taskID, String imgName, JSONArray users) {
        ImgMarkDao imgMarkDao = new ImgMarkDao();
        return imgMarkDao.findAllMarks(taskID, users, imgName).size();
    }

    public boolean postMark(String workerName, Integer taskID) {
        JsonObject json = JsonHelper.openJson(globalJson);
        assert json != null;
        JsonArray taskArray = json.getAsJsonArray(globalTasks);

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder(globalJsonTasks);
        for (Object o : taskArray) {
            Object o1 = o;
            JSONObject jsonObject = new JSONObject(o1.toString());

            if (jsonObject.getInt(globalTaskID) == taskID) {
                //工人标注数量修改
                ArrayList<String> userArray = (ArrayList) jsonObject.getJSONArray(globalUserName).toList();
                JSONArray newArray = new JSONArray();
                for(String str : userArray){
                    String username = str.split("-")[0];
                    if(username.equals(workerName)){
                        username += '-' + String.valueOf(Integer.valueOf(str.split("-")[1]) + 1);
                        newArray.put(username);
                    }
                    else{
                        newArray.put(str);
                    }
                }
                jsonObject.put(globalUserName, newArray);
            }

            newJson.append(jsonObject.toString());
            newJson.append(",");
        }
        if (newJson.lastIndexOf(",") != -1) {
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");

        return this.updateJson(newJson);
    }
}
