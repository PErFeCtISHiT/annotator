package cn.joker.dao;

import cn.joker.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:36 2018/5/6
 */
public interface TaskRepository extends JpaRepository<TaskEntity,Integer>{
}
