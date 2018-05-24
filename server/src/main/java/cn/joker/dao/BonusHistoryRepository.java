package cn.joker.dao;

import cn.joker.entity.BonusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:27 2018/5/18
 */
@Repository
@Table(name = "bonus_history")
public interface BonusHistoryRepository extends JpaRepository<BonusHistoryEntity, Integer> {

}
