package cn.joker.serviceimpl;

import cn.joker.dao.TaskRepository;
import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TagEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.TagService;
import cn.joker.sevice.TaskService;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    private static final String DIR = System.getProperty("user.dir") + "/annotator/";
    private final TaskRepository taskRepository;

    @Resource
    private TagService tagService;

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
            if (taskEntity.getState().equals(status) || status == 0
                    && userRole == 3 && taskEntity.getSponsor().getUsername().equals(userName)) {
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
        List<TagEntity> tagEntities = taskEntity.getTagEntityList();
        for (TagEntity tagEntity : tagEntities) {
            tagEntity.getTaskEntityList().remove(taskEntity);
            tagService.modify(tagEntity);
        }
        taskEntity.setState(2);
        taskEntity.setEndDate(new Date(System.currentTimeMillis()));
        List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
        for (ImageEntity imageEntity : imageEntities) {
            imageEntity.setMarked(true);
        }
        return this.modify(taskEntity);
    }

    @Override
    public boolean deleteTask(Integer taskID) {
        TaskEntity taskEntity = (TaskEntity) findByID(taskID);
        return delete(taskEntity);
    }


    @Override
    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public ResponseEntity<FileSystemResource> getDataSet(TaskEntity taskEntity) {
        File file = new File(DIR + "data/" + taskEntity.getId().toString());
        Boolean bool = true;
        if (!file.getParentFile().getParentFile().exists()) {
            bool = file.getParentFile().getParentFile().mkdir();
        }
        if (!file.getParentFile().exists()) {
            bool = bool && file.getParentFile().mkdir();
        }
        if (!bool)
            return null;
        try (FileWriter fileWriter = new FileWriter(file); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
            for (ImageEntity imageEntity : imageEntities) {
                List<ImgMarkEntity> imgMarkEntities = imageEntity.getImgMarkEntityList();
                for (ImgMarkEntity imgMarkEntity : imgMarkEntities) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(StdName.NOTEPOLYGON,imgMarkEntity.getNotePolygon());
                    jsonObject.put(StdName.NOTETOTAL,imgMarkEntity.getNoteTotal());
                    jsonObject.put(StdName.NOTERECTANGLE,imgMarkEntity.getNoteRectangle());
                    jsonObject.put(StdName.ID,imgMarkEntity.getId());
                    jsonObject.put(StdName.WORKER,imgMarkEntity.getWorker().getUsername());
                    jsonObject.put(StdName.IMGNAME,imgMarkEntity.getImage_imgMark().getImgName());
                    bufferedWriter.write(jsonObject.toString());
                }
            }
            bufferedWriter.close();
            fileWriter.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".txt");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new FileSystemResource(file));

        } catch (IOException e) {
            Logger logger = Logger.getLogger(TaskServiceImpl.class);
            logger.error(StdName.IOEXCEPTION);
            return null;
        }

    }
}