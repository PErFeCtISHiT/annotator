package cn.joker.sevice;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import org.json.JSONObject;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 9:11 2018/4/14
 */
public interface ImgMarkService extends PubService{

    List<ImgMarkEntity> findAll();

    ImgMarkEntity findByImage_imgMarkAndImgMark_taskAndWorker(ImageEntity imageEntity, TaskEntity taskEntity, UserEntity userEntity);
}
