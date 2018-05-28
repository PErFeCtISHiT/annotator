package cn.joker.dao;

import cn.joker.entity.WorkerMatrixEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 18:12 2018/5/28
 */
@Repository
@Table(name = "worker_matrix")
public interface WorkerMatrixRepository extends JpaRepository<WorkerMatrixEntity,Integer>{
}
