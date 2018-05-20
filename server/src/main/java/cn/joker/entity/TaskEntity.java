package cn.joker.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:55 2018/5/4
 */
@Entity
@Table(name = "task", schema = "imgannotator", catalog = "")
public class TaskEntity implements Serializable{
    private Integer id;
    private UserEntity sponsor;
    private String taskName;
    private String description;
    private Integer workerLevel;
    private Integer points;
    private Integer expectedNumber;
    private Integer completedNumber;
    private Integer state;
    private Date startDate;
    private Date endDate;
    private Integer imageNum;
    private List<ImageEntity> imageEntityList;
    private List<BonusHistoryEntity> bonusHistoryEntityList;
    private List<ImgMarkEntity> imgMarkEntityList;
    private List<WorkersForTheTaskEntity> workersForTheTaskEntityList;
    private List<TagEntity> tagEntityList;

    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name="task_tag", joinColumns={@JoinColumn(referencedColumnName="ID")}
            , inverseJoinColumns={@JoinColumn(referencedColumnName="ID")})
    public List<TagEntity> getTagEntityList() {
        return tagEntityList;
    }

    public void setTagEntityList(List<TagEntity> tagEntityList) {
        this.tagEntityList = tagEntityList;
    }

    @OneToMany(mappedBy = "workers_task",cascade = CascadeType.PERSIST)
    public List<WorkersForTheTaskEntity> getWorkersForTheTaskEntityList() {
        return workersForTheTaskEntityList;
    }

    public void setWorkersForTheTaskEntityList(List<WorkersForTheTaskEntity> workersForTheTaskEntityList) {
        this.workersForTheTaskEntityList = workersForTheTaskEntityList;
    }

    @OneToMany(mappedBy = "imgMark_task",cascade = CascadeType.PERSIST)
    public List<ImgMarkEntity> getImgMarkEntityList() {
        return imgMarkEntityList;
    }

    public void setImgMarkEntityList(List<ImgMarkEntity> imgMarkEntityList) {
        this.imgMarkEntityList = imgMarkEntityList;
    }

    @OneToMany(mappedBy = "img_task",cascade = CascadeType.PERSIST)
    public List<ImageEntity> getImageEntityList() {
        return imageEntityList;
    }

    public void setImageEntityList(List<ImageEntity> imageEntityList) {
        this.imageEntityList = imageEntityList;
    }



    @OneToMany(mappedBy = "bonusHistory_task",cascade = CascadeType.PERSIST)
    public List<BonusHistoryEntity> getBonusHistoryEntityList() {
        return bonusHistoryEntityList;
    }

    public void setBonusHistoryEntityList(List<BonusHistoryEntity> bonusHistoryEntityList) {
        this.bonusHistoryEntityList = bonusHistoryEntityList;
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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    public UserEntity getSponsor() {
        return sponsor;
    }

    public void setSponsor(UserEntity sponsor) {
        this.sponsor = sponsor;
    }

    @Basic
    @Column(name = "taskName", nullable = false, length = 200)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Basic
    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "workerLevel", nullable = true)
    public Integer getWorkerLevel() {
        return workerLevel;
    }

    public void setWorkerLevel(Integer workerLevel) {
        this.workerLevel = workerLevel;
    }

    @Basic
    @Column(name = "points", nullable = true)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Basic
    @Column(name = "expectedNumber", nullable = true)
    public Integer getExpectedNumber() {
        return expectedNumber;
    }

    public void setExpectedNumber(Integer expectedNumber) {
        this.expectedNumber = expectedNumber;
    }

    @Basic
    @Column(name = "completedNumber", nullable = true)
    public Integer getCompletedNumber() {
        return completedNumber;
    }

    public void setCompletedNumber(Integer completedNumber) {
        this.completedNumber = completedNumber;
    }

    @Basic
    @Column(name = "state", nullable = true)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "startDate", nullable = true)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "endDate", nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "imageNum", nullable = true)
    public Integer getImageNum() {
        return imageNum;
    }

    public void setImageNum(Integer imageNum) {
        this.imageNum = imageNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sponsor, that.sponsor) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(workerLevel, that.workerLevel) &&
                Objects.equals(points, that.points) &&
                Objects.equals(expectedNumber, that.expectedNumber) &&
                Objects.equals(completedNumber, that.completedNumber) &&
                Objects.equals(state, that.state) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(imageNum, that.imageNum) &&
                Objects.equals(imageEntityList, that.imageEntityList) &&
                Objects.equals(bonusHistoryEntityList, that.bonusHistoryEntityList) &&
                Objects.equals(imgMarkEntityList, that.imgMarkEntityList) &&
                Objects.equals(workersForTheTaskEntityList, that.workersForTheTaskEntityList) &&
                Objects.equals(tagEntityList, that.tagEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sponsor, taskName, description, workerLevel, points, expectedNumber, completedNumber, state, startDate, endDate, imageNum, imageEntityList, bonusHistoryEntityList, imgMarkEntityList, workersForTheTaskEntityList, tagEntityList);
    }
}
