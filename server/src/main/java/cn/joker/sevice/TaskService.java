package cn.joker.sevice;

import cn.joker.entity.Task;

import java.util.List;

public interface TaskService {
    public boolean releaseTask(Task task);

    public boolean modifyTask(Task task);

    public List<Task> checkMyTask(String userName, Integer status, Integer userRole);

    public List<Task> search(int userRole, String tag, Integer status);

    public boolean endTask(Integer taskID);

    public boolean deleteTask(Integer taskID);

    public boolean completeTask(Integer taskID, String workerName);

    public boolean abortTask(Integer taskID, String workerName);

    public boolean acceptTask(Integer taskID, String workerName);

    //目前还不确定，好像这个返回值有点多
    public Task checkTaskDetail(Integer taskID, Integer userRole);

    //查看某个工人的进度
    public Double checkTaskProgress(Integer taskID,String workerName);


    //得到一个新ID
    public Integer generateID();
}
