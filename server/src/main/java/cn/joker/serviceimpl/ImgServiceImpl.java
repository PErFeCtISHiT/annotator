package cn.joker.serviceimpl;

import cn.joker.dao.ImageRepository;
import cn.joker.entity.ImageEntity;
import cn.joker.sevice.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 13:23 2018/5/20
 */
@Service
public class ImgServiceImpl extends PubServiceImpl implements ImgService {
    private final ImageRepository imageRepository;

    /**
     *
     * @param imageRepository 图片数据库
     */
    @Autowired
    public ImgServiceImpl(ImageRepository imageRepository) {
        this.repository = imageRepository;
        this.imageRepository = imageRepository;
    }

    /**
     *
     * @param url 地址
     * @return 图片
     */
    @Override
    public ImageEntity findByUrl(String url) {
        return imageRepository.findByUrl(url);
    }

    /**
     *
     * @param name 名字
     * @return 图片
     */
    @Override
    public ImageEntity findByName(String name) {
        return imageRepository.findByImgName(name);
    }
}
