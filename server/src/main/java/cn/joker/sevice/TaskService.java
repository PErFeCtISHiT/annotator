package cn.joker.sevice;

import cn.joker.entity.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface TaskService {
    boolean releaseTask(Task task);

    boolean modifyTask(Task task);

    List<Task> checkMyTask(String userName, Integer status, Integer userRole);

    List<Task> search(int userRole, String tag, Integer status);

    boolean endTask(Integer taskID);

    boolean deleteTask(Integer taskID);

    boolean completeTask(Integer taskID, String workerName);

    boolean abortTask(Integer taskID, String workerName);

    boolean acceptTask(Integer taskID, String workerName);

    //目前还不确定，好像这个返回值有点多
    JSONObject checkTaskDetail(Integer taskID);

    //查看某个工人的进度
    Double checkTaskProgress(Integer taskID, String workerName);

    List findImgURLByID(String taskID);

    Integer findMarkNumByImgNameAndUserAndID(Integer taskID, String imgName, JSONArray users);
}
