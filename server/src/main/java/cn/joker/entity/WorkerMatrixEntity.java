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
public class WorkerMatrixEntity implements Serializable {
    private Double C00;
    private Double C01;
    private Double C10;
    private Double C11;
    private UserEntity user_matrix;
    private Integer id;
    private Double num;

    @Basic
    @Column(name = "num")
    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    @Transient
    private Double correct;

    public Double getCorrect() {
        return (C00 + C11) / (C00 + C01 + C10 + C11);
    }

    public WorkerMatrixEntity(UserEntity userEntity,Double rate,Integer num) {
        C00 = C11 = rate * num / 2;
        C01 = C10 = (1 - rate) * num / 2;
        user_matrix = userEntity;
        num = 0;
    }

    public void setCorrect(Double correct) {
        this.correct = correct;
    }

    public WorkerMatrixEntity() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "c00")
    public Double getC00() {
        return C00;
    }

    public void setC00(Double c00) {
        C00 = c00;
    }

    @Basic
    @Column(name = "c01")
    public Double getC01() {
        return C01;
    }

    public void setC01(Double c01) {
        C01 = c01;
    }

    @Basic
    @Column(name = "c10")
    public Double getC10() {
        return C10;
    }

    public void setC10(Double c10) {
        C10 = c10;
    }

    @Basic
    @Column(name = "c11")
    public Double getC11() {
        return C11;
    }

    public void setC11(Double c11) {
        C11 = c11;
    }


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public UserEntity getUser_matrix() {
        return user_matrix;
    }

    public void setUser_matrix(UserEntity user_matrix) {
        this.user_matrix = user_matrix;
    }
}
