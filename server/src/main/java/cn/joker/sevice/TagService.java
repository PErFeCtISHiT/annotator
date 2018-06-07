package cn.joker.sevice;

import cn.joker.entity.TagEntity;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:07 2018/5/20
 */
public interface TagService extends PubService {
    TagEntity findByTag(String tag);

    boolean refreshTest(TagEntity tagEntity);

    boolean markIntegration(TagEntity tagEntity);

    List<TagEntity> findAll();
}
