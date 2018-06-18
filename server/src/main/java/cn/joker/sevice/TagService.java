package cn.joker.sevice;

import cn.joker.entity.TagEntity;
import cn.joker.entity.UserEntity;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:07 2018/5/20
 */
public interface TagService extends PubService {
    /**
     *
     * @param tag 标签
     * @return 标签信息
     */
    TagEntity findByTag(String tag);

    /**
     *
     * @param userEntity 用户
     * @param tagEntity 标签
     * @param type 标注类型
     * @return 整合结果，能否继续标注
     */
    boolean markIntegration(UserEntity userEntity, TagEntity tagEntity, Integer type);

    /**
     *
     * @return 所有的标签信息
     */
    List<TagEntity> findAll();

}
