package cn.joker66.entity;

import java.io.Serializable;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 14:21 2018/4/10
 */
public class BonusHistory implements Serializable{
    private Integer taskID;
    private String workerName;
    private Integer points;

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
