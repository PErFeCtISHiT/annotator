package cn.joker.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable{
    //任务有ID
    private Integer taskID;//任务ID，任务识别标识

    private String sponsorName;//发起者名字
    private String taskName;//任务名字
    private String description;//任务描述
    private String[] tag;//图片标签，便于搜索任务

    private Integer workerLevel;//用户等级要求，可选项
    private Integer points;//用户完成任务的积分奖励
    private Integer expectedNumber;//预计完成数量要求，满足该要求自动关闭任务
    private Integer completedNumber;//目前已完成数量

    private Date startDate;//开始时间
    private Date endDate;//结束时间

    //可能不需要该属性
    //private String imgName;//图片包名字

    private ArrayList<String> userName;//标注用户的用户名

    public Task() {
    }

    public Integer getCompletedNumber() {
        return completedNumber;
    }

    public void setCompletedNumber(Integer completedNumber) {
        this.completedNumber = completedNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getExpectedNumber() {
        return expectedNumber;
    }

    public void setExpectedNumber(Integer exceptedNumber) {
        this.expectedNumber = expectedNumber;
    }

    public void setUserName(ArrayList<String> userName) {
        this.userName = userName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public Integer getWorkerLevel() {
        return workerLevel;
    }

    public void setWorkerLevel(Integer workerLevel) {
        this.workerLevel = workerLevel;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }
}
