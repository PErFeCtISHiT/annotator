package cn.joker.sevice;

import cn.joker.entity.TestEntity;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 3:24 2018/6/16
 */
public interface TestService extends PubService{
    List<TestEntity> findAll();
}
