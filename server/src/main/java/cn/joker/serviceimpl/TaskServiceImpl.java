package cn.joker.serviceimpl;

import cn.joker.dao.TaskDao;
import cn.joker.entity.Task;
import cn.joker.sevice.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService{
    private TaskDao taskDao = new TaskDao();

    @Override
    public boolean releaseTask(Task task) {
        return taskDao.releaseTask(task);
    }

    @Override
    public boolean modifyTask(Task task) {
        return taskDao.modifyTask(task);
    }

    @Override
    public List<Task> checkMyTask(String userName, Integer status, Integer userRole) {
        return taskDao.checkMyTask(userName, status, userRole);
    }

    @Override
    public List<Task> search(int userRole, String tag, Integer status) {
        return taskDao.search(userRole, tag, status);
    }

    @Override
    public boolean endTask(Integer taskID) {
        return taskDao.endTask(taskID);
    }

    @Override
    public boolean deleteTask(Integer taskID) {
        return taskDao.deleteTask(taskID);
    }

    @Override
    public boolean completeTask(Integer taskID, String workerName) {
        return taskDao.completeTask(taskID, workerName);
    }

    @Override
    public boolean abortTask(Integer taskID, String workerName) {
        return taskDao.abortTask(taskID, workerName);
    }

    @Override
    public boolean acceptTask(Integer taskID, String workerName) {
        return taskDao.acceptTask(taskID, workerName);
    }

    @Override
    public Task checkTaskDetail(Integer taskID, Integer userRole) {
        return taskDao.checkTaskDetail(taskID, userRole);
    }
}
