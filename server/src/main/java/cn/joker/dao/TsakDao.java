package cn.joker.dao;

import cn.joker.entity.Task;

import java.util.List;

public class TsakDao {
    public boolean releaseTask(Task task){
        return false;
    }

    public boolean modifyTask(Task task){
        return false;
    }

    public List<Task> checkMyTask(String userName, Integer status, Integer userRole){
        return null;
    }

    public List<Task> search(int userRole, String tag, Integer status){
        return null;
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
}
