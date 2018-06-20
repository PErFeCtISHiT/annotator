package cn.joker.dao;

import cn.joker.entity.AbilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:28 2018/5/18
 */
@Repository
@Table(name = "ability")
public interface AbilityRepository extends JpaRepository<AbilityEntity, Integer> {
}
