package cn.joker.sevice;

import cn.joker.entity.TaskEntity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService extends PubService {
    /**
     *
     * @param task 任务
     * @return 是否标注成功
     */
    Integer releaseTask(TaskEntity task);

    /**
     *
     * @param userName 用户名字
     * @param status 状态
     * @param userRole 角色
     * @param tag 标签
     * @return 任务列表
     */
    List<TaskEntity> checkMyTask(String userName, Integer status, Integer userRole, String tag);

    /**
     *
     * @param userRole 角色
     * @param tag 标签
     * @param status 状态
     * @return 任务列表
     */
    List<TaskEntity> search(int userRole, String tag, Integer status);

    /**
     *
     * @param taskID 任务id
     * @return 是否成功
     */
    boolean endTask(Integer taskID);

    /**
     *
     * @param taskID  任务id
     * @return 是否成功
     */
    boolean deleteTask(Integer taskID);

    /**
     *
     * @return 所有任务列表
     */
    List<TaskEntity> findAll();

    /**
     *
     * @param taskEntity 任务信息
     * @return 下载任务
     */
    ResponseEntity<FileSystemResource> getDataSet(TaskEntity taskEntity);
}
