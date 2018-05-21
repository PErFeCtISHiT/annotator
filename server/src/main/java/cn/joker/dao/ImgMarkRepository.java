package cn.joker.dao;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:30 2018/5/18
 */
@Repository
@Table(name = "img_mark")
public interface ImgMarkRepository extends JpaRepository<ImgMarkEntity,Integer> {
}
