package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:55 2018/5/4
 */
@Entity
@Table(name = "ability", schema = "imgannotator", catalog = "")
public class AbilityEntity implements Serializable {
    private Integer id;
    private Double totalPoints;
    private List<UserEntity> userEntityList;

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
    @Column(name = "totalPoints", nullable = true, precision = 0)
    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    @ManyToMany(mappedBy = "abilityEntityList", cascade = CascadeType.MERGE)
    @JsonIgnore
    public List<UserEntity> getUserEntityList() {
        return userEntityList;
    }

    public void setUserEntityList(List<UserEntity> userEntityList) {
        this.userEntityList = userEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbilityEntity that = (AbilityEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(totalPoints, that.totalPoints) &&
                Objects.equals(userEntityList, that.userEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, totalPoints, userEntityList);
    }
}
