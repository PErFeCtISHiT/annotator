package cn.joker.serviceimpl;

import cn.joker.dao.ImgMarkRepository;
import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.sevice.ImgMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 13:41 2018/5/20
 */
@Service
public class ImgMarkServiceImpl extends PubServiceImpl implements ImgMarkService {
    private final ImgMarkRepository imgMarkRepository;

    @Autowired
    public ImgMarkServiceImpl(ImgMarkRepository imgMarkRepository) {
        this.repository = imgMarkRepository;
        this.imgMarkRepository = imgMarkRepository;
    }

    @Override
    public List<ImgMarkEntity> findAll() {
        return imgMarkRepository.findAll();
    }

    @Override
    public ImgMarkEntity findByImage_imgMarkAndImgMark_taskAndWorker(ImageEntity imageEntity, TaskEntity taskEntity, UserEntity userEntity) {
        return null;
        // return imgMarkRepository.findByImage_imgMarkAndImgMark_taskAndWorker(imageEntity, taskEntity, userEntity);
    }
}
