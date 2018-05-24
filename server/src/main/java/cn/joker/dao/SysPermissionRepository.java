package cn.joker.dao;

import cn.joker.entity.SysPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 12:58 2018/5/5
 */
@Repository
@Table(name = "sys_permission")
public interface SysPermissionRepository extends JpaRepository<SysPermissionEntity, Integer> {
}
