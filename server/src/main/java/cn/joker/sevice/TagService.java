package cn.joker.sevice;

import cn.joker.entity.TagEntity;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:07 2018/5/20
 */
public interface TagService extends PubService{
    TagEntity findByTag(String tag);
}
