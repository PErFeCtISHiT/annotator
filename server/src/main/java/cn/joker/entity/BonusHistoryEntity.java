package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:55 2018/5/4
 */
@Entity
@Table(name = "bonus_history", schema = "imgannotator", catalog = "")
public class BonusHistoryEntity implements Serializable {
    private Integer id;
    private TaskEntity bonusHistory_task;
    private Integer points;
    private UserEntity bonusHistory_user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    public TaskEntity getBonusHistory_task() {
        return bonusHistory_task;
    }

    public void setBonusHistory_task(TaskEntity bonusHistory_task) {
        this.bonusHistory_task = bonusHistory_task;
    }

    @Basic
    @Column(name = "points", nullable = true)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public UserEntity getBonusHistory_user() {
        return bonusHistory_user;
    }

    public void setBonusHistory_user(UserEntity bonusHistory_user) {
        this.bonusHistory_user = bonusHistory_user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BonusHistoryEntity that = (BonusHistoryEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(bonusHistory_task, that.bonusHistory_task) &&
                Objects.equals(points, that.points) &&
                Objects.equals(bonusHistory_user, that.bonusHistory_user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, bonusHistory_task, points, bonusHistory_user);
    }
}
