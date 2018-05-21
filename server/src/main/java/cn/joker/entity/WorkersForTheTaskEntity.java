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
@Table(name = "workers_for_the_task", schema = "imgannotator", catalog = "")
public class WorkersForTheTaskEntity implements Serializable{
    private Integer id;
    private UserEntity worker;
    private Integer markedNum;
    private TaskEntity workers_task;
    private Integer completedNum;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    public TaskEntity getWorkers_task() {
        return workers_task;
    }

    public void setWorkers_task(TaskEntity workers_task) {
        this.workers_task = workers_task;
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public UserEntity getWorker() {
        return worker;
    }

    public void setWorker(UserEntity worker) {
        this.worker = worker;
    }

    @Basic
    @Column(name = "markedNum", nullable = true)
    public Integer getMarkedNum() {
        return markedNum;
    }

    public void setMarkedNum(Integer markedNum) {
        this.markedNum = markedNum;
    }

    @Basic
    @Column(name = "completedNum", nullable = true)
    public Integer getCompletedNum() {
        return completedNum;
    }

    public void setCompletedNum(Integer completedNum) {
        this.completedNum = completedNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkersForTheTaskEntity that = (WorkersForTheTaskEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(worker, that.worker) &&
                Objects.equals(markedNum, that.markedNum) &&
                Objects.equals(workers_task, that.workers_task);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, worker, markedNum, workers_task);
    }
}
