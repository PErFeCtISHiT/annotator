package cn.joker.serviceimpl;

import cn.joker.dao.TaskRepository;
import cn.joker.entity.TaskEntity;
import cn.joker.sevice.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:34 2018/5/6
 */
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public Integer releaseTask(TaskEntity task) {
        return taskRepository.save(task).getId();
    }

    @Override
    public boolean modifyTask(TaskEntity task) {
        return taskRepository.save(task) != null;
    }

    @Override
    public List<TaskEntity> checkMyTask(String userName, Integer status, Integer userRole, String tag) {
        return null;
    }

    @Override
    public List<TaskEntity> search(int userRole, String tag, Integer status) {
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
    public JSONObject checkTaskDetail(Integer taskID) {
        return null;
    }

    @Override
    public Double checkTaskProgress(Integer taskID, String workerName) {
        return null;
    }

    @Override
    public List findImgURLByID(String taskID) {
        return null;
    }

    @Override
    public Integer findMarkNumByImgNameAndUserAndID(Integer taskID, String imgName, JSONArray users) {
        return null;
    }

    @Override
    public boolean postMark(String workerName, Integer taskID) {
        return false;
    }
}
