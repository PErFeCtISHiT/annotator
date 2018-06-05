package cn.joker.serviceimpl;

import cn.joker.dao.ImgMarkRepository;
import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.sevice.ImgMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<ImgMarkEntity> findByImage(ImageEntity imageEntity) {
        List<ImgMarkEntity> imgMarkEntities = findAll();
        List<ImgMarkEntity> ret = new ArrayList<>();
        for (ImgMarkEntity imgMarkEntity : imgMarkEntities) {
            if (imgMarkEntity.getImage_imgMark().getId().equals(imageEntity.getId()))
                ret.add(imgMarkEntity);
        }
        return ret;
    }
}
