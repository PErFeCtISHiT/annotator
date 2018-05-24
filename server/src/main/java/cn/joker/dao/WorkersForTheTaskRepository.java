package cn.joker.dao;

import cn.joker.entity.WorkersForTheTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:33 2018/5/18
 */
@Repository
@Table(name = "workers_for_the_task")
public interface WorkersForTheTaskRepository extends JpaRepository<WorkersForTheTaskEntity, Integer> {
}
