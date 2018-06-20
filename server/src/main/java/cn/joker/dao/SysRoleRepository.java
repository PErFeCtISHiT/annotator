package cn.joker.dao;

import cn.joker.entity.SysRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 12:53 2018/5/5
 */
@Repository
@Table(name = "sys_role")
public interface SysRoleRepository extends JpaRepository<SysRoleEntity, Integer> {
}
