package cn.joker.dao;

import cn.joker.entity.ImgMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:30 2018/5/18
 */
@Repository
@Table(name = "img_mark")
public interface ImgMarkRepository extends JpaRepository<ImgMarkEntity, Integer> {
}
