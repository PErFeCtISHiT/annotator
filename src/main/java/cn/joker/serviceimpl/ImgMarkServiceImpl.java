package cn.joker.serviceimpl;

import cn.joker.dao.ImgMarkDao;
import cn.joker.entity.ImgMark;
import cn.joker.sevice.ImgMarkService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 9:12 2018/4/14
 */
@Service
public class ImgMarkServiceImpl implements ImgMarkService {
    @Resource
    private ImgMarkDao imgMarkDao;

    @Override
    public boolean refreshMark(ImgMark imgMark, JSONObject jsonObject) {
        return imgMarkDao.refreshMark(imgMark, jsonObject);
    }

    @Override
    public List<ImgMark> findAllMarks(JSONObject jsonObject) {
        return imgMarkDao.findAllMarks(jsonObject);
    }
}
