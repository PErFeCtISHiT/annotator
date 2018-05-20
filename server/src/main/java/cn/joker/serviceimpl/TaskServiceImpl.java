package cn.joker.serviceimpl;

import cn.joker.dao.TaskRepository;
import cn.joker.entity.TaskEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:34 2018/5/6
 */
@Service
@Transactional(readOnly = true,propagation = Propagation.NESTED)
@CacheConfig
public class TaskServiceImpl extends PubServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.repository = taskRepository;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "secondlevels", allEntries = true)
    public Integer releaseTask(TaskEntity task) {
        return taskRepository.saveAndFlush(task).getId();
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
