package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 18:55 2018/5/27
 */
@Entity
@Table(name = "worker_matrix", schema = "imgannotator", catalog = "")
public class WorkerMatrixEntity implements Serializable{
    private Double C00;
    private Double C01;
    private Double C10;
    private Double C11;
    private UserEntity userEntity;

    public WorkerMatrixEntity() {
        C00 = C01 = C10 = C11 = 1.0;
    }

    public Double getC00() {
        return C00;
    }

    public void setC00(Double c00) {
        C00 = c00;
    }

    public Double getC01() {
        return C01;
    }

    public void setC01(Double c01) {
        C01 = c01;
    }

    public Double getC10() {
        return C10;
    }

    public void setC10(Double c10) {
        C10 = c10;
    }

    public Double getC11() {
        return C11;
    }

    public void setC11(Double c11) {
        C11 = c11;
    }


    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
