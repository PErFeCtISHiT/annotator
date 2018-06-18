package cn.joker.sevice;

import cn.joker.entity.ImageEntity;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 13:22 2018/5/20
 */
public interface ImgService extends PubService {
    /**
     *
     * @param url 路径
     * @return 图片
     */
    ImageEntity findByUrl(String url);

    /**
     *
     * @param name 名字
     * @return 图片
     */
    ImageEntity findByName(String name);
}
