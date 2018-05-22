package cn.joker.dao;

import cn.joker.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:29 2018/5/18
 */
@Repository
@Table(name = "image")
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    ImageEntity findByUrl(String url);

    ImageEntity findByImgName(String imgName);
}
