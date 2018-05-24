package cn.joker.dao;

import cn.joker.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:32 2018/5/18
 */
@Repository
@Table(name = "tag")
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    TagEntity findByTag(String tag);
}
