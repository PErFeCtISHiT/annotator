package cn.joker.serviceimpl;

import cn.joker.dao.TaskRepository;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.entity.WorkersForTheTaskEntity;
import cn.joker.sevice.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:34 2018/5/6
 */
@Service
public class TaskServiceImpl extends PubServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.repository = taskRepository;
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
    public boolean postMark(UserEntity userEntity, TaskEntity taskEntity) {
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
            if (workersForTheTaskEntity.getWorker().getUsername().equals(userEntity.getUsername())) {
                workersForTheTaskEntity.setMarkedNum(workersForTheTaskEntity.getMarkedNum() + 1);
                break;
            }
        }
        taskEntity.setWorkersForTheTaskEntityList(workersForTheTaskEntities);
        return this.modify(taskEntity);
    }

    @Override
    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }


}
