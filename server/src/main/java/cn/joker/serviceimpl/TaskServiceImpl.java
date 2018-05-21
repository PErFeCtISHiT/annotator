package cn.joker.serviceimpl;

import cn.joker.dao.TaskRepository;
import cn.joker.entity.TagEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.entity.WorkersForTheTaskEntity;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:34 2018/5/6
 */
@Service
public class TaskServiceImpl extends PubServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Resource
    private UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.repository = taskRepository;
    }

    @Override
    public Integer releaseTask(TaskEntity task) {
        if (taskRepository.saveAndFlush(task) != null)
            return task.getId();
        else
            return -1;
    }

    @Override
    public List<TaskEntity> checkMyTask(String userName, Integer status, Integer userRole, String tag) {
        UserEntity userEntity = userService.findByUsername(userName);
        List<TaskEntity> ret = new ArrayList<>();
        List<TaskEntity> taskEntities = taskRepository.findAll();
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getState().equals(status)) {
                if (userRole == 2 && taskEntity.getSponsor().getUsername().equals(userName)) {
                    if (tag.length() == 0)
                        ret.add(taskEntity);
                    else {
                        List<TagEntity> tagEntities = taskEntity.getTagEntityList();
                        for (TagEntity tagEntity : tagEntities) {
                            if (tagEntity.getTag().equals(tag)) {
                                ret.add(taskEntity);
                                break;
                            }
                        }
                    }
                } else if (userRole == 3) {
                    List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
                    for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
                        if (workersForTheTaskEntity.getWorker().getUsername().equals(userName)) {
                            if (tag.length() == 0) {
                                ret.add(taskEntity);
                                break;
                            } else {
                                List<TagEntity> tagEntities = taskEntity.getTagEntityList();
                                for (TagEntity tagEntity : tagEntities) {
                                    if (tagEntity.getTag().equals(tag)) {
                                        ret.add(taskEntity);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;
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
    public Double checkTaskProgress(Integer taskID, String workerName) {
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
