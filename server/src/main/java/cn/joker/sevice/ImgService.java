package cn.joker.sevice;

import cn.joker.entity.ImageEntity;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 13:22 2018/5/20
 */
public interface ImgService extends PubService {
    ImageEntity findByUrl(String url);

    ImageEntity findByName(String name);
}
