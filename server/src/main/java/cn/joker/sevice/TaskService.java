package cn.joker.sevice;

import cn.joker.entity.TaskEntity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService extends PubService {
    Integer releaseTask(TaskEntity task);


    List<TaskEntity> checkMyTask(String userName, Integer status, Integer userRole, String tag);

    List<TaskEntity> search(int userRole, String tag, Integer status);

    boolean endTask(Integer taskID);

    boolean deleteTask(Integer taskID);

    List<TaskEntity> findAll();

    ResponseEntity<FileSystemResource> getDataSet(TaskEntity taskEntity);
}
