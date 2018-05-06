package cn.joker.sevice;

import cn.joker.entity.ImageEntity;
import org.json.JSONObject;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 9:11 2018/4/14
 */
public interface ImgMarkService {
    boolean refreshMark(ImageEntity imgMark, JSONObject jsonObject);

    List<ImageEntity> findAllMarks(JSONObject jsonObject);
}
