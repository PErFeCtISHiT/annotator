package cn.joker.dao;

import cn.joker.entity.ReportmessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:31 2018/5/18
 */
@Repository
@Table(name = "reportmessage")
public interface ReportmessageRepository extends JpaRepository<ReportmessageEntity, Integer> {
    public List<ReportmessageEntity> findByType(Integer type);
}
