package cn.joker.sevice;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 9:11 2018/4/14
 */
public interface ImgMarkService extends PubService {
    /**
     *
     * @return 标注列表
     */
    List<ImgMarkEntity> findAll();

    /**
     *
     * @param imageEntity 图片
     * @return 该张图片的标注
     */
    List<ImgMarkEntity> findByImage(ImageEntity imageEntity);
}
