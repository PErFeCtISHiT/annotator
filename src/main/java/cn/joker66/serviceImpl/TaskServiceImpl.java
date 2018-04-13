package cn.joker66.serviceImpl;

import cn.joker66.entity.Task;
import cn.joker66.sevice.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService{
    @Override
    public boolean releaseTask(Task task) {
        return false;
    }

    @Override
    public boolean modifyTask(Task task) {
        return false;
    }

    @Override
    public List<Task> checkMyTask(String userName, Integer status, Integer userRole) {
        return null;
    }

    @Override
    public List<Task> search(int userRole, String tag, Integer status) {
        return null;
    }

    @Override
    public boolean endTask(Integer taskID) {
        return false;
    }

    @Override
    public boolean deleteTask(Integer taskID) {
        return false;
    }

    @Override
    public boolean completeTask(Integer taskID, String workerName) {
        return false;
    }

    @Override
    public boolean abortTask(Integer taskID, String workerName) {
        return false;
    }

    @Override
    public boolean acceptTask(Integer taskID, String workerName) {
        return false;
    }

    @Override
    public Task checkTaskDetail(Integer taskID, Integer userRole) {
        return null;
    }
}
