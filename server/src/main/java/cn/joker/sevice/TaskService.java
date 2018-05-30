package cn.joker.sevice;

import cn.joker.entity.TaskEntity;

import java.util.List;

public interface TaskService extends PubService {
    Integer releaseTask(TaskEntity task);


    List<TaskEntity> checkMyTask(String userName, Integer status, Integer userRole, String tag);

    List<TaskEntity> search(int userRole, String tag, Integer status);

    boolean endTask(Integer taskID);

    boolean deleteTask(Integer taskID);

    boolean completeTask(Integer taskID, String workerName);

    boolean abortTask(Integer taskID, String workerName);

    boolean acceptTask(Integer taskID, String workerName);

    List<TaskEntity> findAll();
}
