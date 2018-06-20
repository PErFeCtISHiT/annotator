package cn.joker.dao;

import cn.joker.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 3:23 2018/6/16
 */
@Repository
@Table(name = "test")
public interface TestRepository extends JpaRepository<TestEntity, Integer> {
}
