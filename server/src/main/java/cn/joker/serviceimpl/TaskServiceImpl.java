package cn.joker.serviceimpl;

import cn.joker.dao.TaskRepository;
import cn.joker.entity.*;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.statisticalMethod.NaiveBayesianClassification;
import cn.joker.vo.RecNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
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
        List<TaskEntity> ret = new ArrayList<>();
        List<TaskEntity> taskEntities = taskRepository.findAll();
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getState().equals(status) || status == 0) {
                if (userRole == 3 && taskEntity.getSponsor().getUsername().equals(userName)) {
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
                } else if (userRole == 4) {
                    int flag = 0;
                    List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
                    for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
                        if (workersForTheTaskEntity.getWorker().getUsername().equals(userName) && workersForTheTaskEntity.getIsFinished() == 1) {
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        continue;
                    workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
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
        List<TaskEntity> ret = new ArrayList<>();
        List<TaskEntity> taskEntities = taskRepository.findAll();
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getState().equals(status) || status == 0) {
                if (tag.length() == 0) {
                    ret.add(taskEntity);
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
        return ret;
    }

    @Override
    public boolean endTask(Integer taskID) {
        TaskEntity taskEntity = (TaskEntity) this.findByID(taskID);
        taskEntity.setState(2);
        taskEntity.setEndDate((Date) new java.util.Date());
        return this.modify(taskEntity);
    }

    @Override
    public boolean deleteTask(Integer taskID) {
        TaskEntity taskEntity = (TaskEntity) findByID(taskID);
        return delete(taskEntity);
    }

    @Override
    public boolean completeTask(Integer taskID, String workerName) {
        TaskEntity taskEntity = (TaskEntity) findByID(taskID);
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        if (workersForTheTaskEntities == null)
            workersForTheTaskEntities = new ArrayList<>();
        for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
            if (workersForTheTaskEntity.getWorker().getUsername().equals(workerName)) {
                workersForTheTaskEntity.setCompletedNum(workersForTheTaskEntity.getCompletedNum() + 1);
                workersForTheTaskEntity.setIsFinished(1);
                break;
            }
        }
        taskEntity.setCompletedNumber(taskEntity.getCompletedNumber() + 1);
        taskEntity.setWorkersForTheTaskEntityList(workersForTheTaskEntities);
        return modify(taskEntity);
    }

    @Override
    public boolean abortTask(Integer taskID, String workerName) {
        TaskEntity taskEntity = (TaskEntity) findByID(taskID);
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        if (workersForTheTaskEntities == null)
            workersForTheTaskEntities = new ArrayList<>();
        for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
            if (workersForTheTaskEntity.getWorker().getUsername().equals(workerName)) {
                workersForTheTaskEntities.remove(workersForTheTaskEntity);
                break;
            }
        }
        taskEntity.setWorkersForTheTaskEntityList(workersForTheTaskEntities);
        return modify(taskEntity);
    }

    @Override
    public boolean acceptTask(Integer taskID, String workerName) {
        TaskEntity taskEntity = (TaskEntity) findByID(taskID);
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        if (workersForTheTaskEntities == null)
            workersForTheTaskEntities = new ArrayList<>();
        UserEntity userEntity = userService.findByUsername(workerName);
        WorkersForTheTaskEntity workersForTheTaskEntity = new WorkersForTheTaskEntity();
        workersForTheTaskEntity.setIsFinished(0);
        workersForTheTaskEntity.setCompletedNum(0);
        workersForTheTaskEntity.setMarkedNum(0);
        workersForTheTaskEntity.setWorkers_task(taskEntity);
        workersForTheTaskEntity.setWorker(userEntity);
        workersForTheTaskEntities.add(workersForTheTaskEntity);
        taskEntity.setWorkersForTheTaskEntityList(workersForTheTaskEntities);
        return modify(taskEntity);
    }

    @Override
    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public ImgMarkEntity markIntegration(Integer taskID) {
        NaiveBayesianClassification naiveBayesianClassification = new NaiveBayesianClassification();
        List<RecNode> recNodes = naiveBayesianClassification.getAllRecNode();

    }



}
