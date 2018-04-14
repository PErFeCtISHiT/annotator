package cn.joker66.sevice;

import cn.joker66.entity.ImgMark;
import org.json.JSONObject;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 9:11 2018/4/14
 */
public interface ImgMarkService {
    boolean refreshMark(ImgMark imgMark, JSONObject jsonObject);

    List<ImgMark> findAllMarks(JSONObject jsonObject);
}
