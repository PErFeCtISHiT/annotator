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
//    @Query("select ImgMarkEntity from ImgMarkEntity join ImgMarkEntity.image_imgMark image join ImgMarkEntity.imgMark_task task join " +
//            "ImgMarkEntity.worker worker where image=:image and task=:task and worker=:user")
//    ImgMarkEntity findByImage_imgMarkAndImgMark_taskAndWorker(@Param("image") ImageEntity imageEntity, @Param("task") TaskEntity taskEntity,@Param("user") UserEntity userEntity);
}
