package cn.joker.serviceimpl;

import cn.joker.dao.TaskDao;
import cn.joker.entity.Task;
import cn.joker.sevice.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskDao taskDao = new TaskDao();

    @Override
    public Integer releaseTask(Task task) {
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
    public JSONObject checkTaskDetail(Integer taskID) {
        return taskDao.checkTaskDetail(taskID);
    }

    @Override
    public Double checkTaskProgress(Integer taskID, String workerName) {
        return taskDao.checkTaskProgress(taskID, workerName);
    }

    @Override
    public ArrayList<String> findImgURLByID(String taskID) {
        return taskDao.findImgURLByID(taskID);
    }

    @Override
    public Integer findMarkNumByImgNameAndUserAndID(Integer taskID, String imgName, JSONArray users) {
        return taskDao.findMarkNumByImgNameAndUser(taskID, imgName, users);
    }


}
