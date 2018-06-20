package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
public class TaskEntity implements Serializable {
    private Integer id;
    private UserEntity sponsor;
    private String taskName;
    private String description;
    private Integer workerLevel;
    private Integer state;
    private Date startDate;
    private Date endDate;
    private Integer imageNum;
    private Integer type;
    private Integer polygonNum;
    private Integer actNum;
    private List<ImageEntity> imageEntityList;
    private List<TagEntity> tagEntityList;
    @Basic
    @Column(name = "actNum", nullable = true)
    public Integer getActNum() {
        return actNum;
    }

    public void setActNum(Integer actNum) {
        this.actNum = actNum;
    }

    @Basic
    @Column(name = "polygonNum", nullable = true)
    public Integer getPolygonNum() {
        return polygonNum;
    }

    public void setPolygonNum(Integer polygonNum) {
        this.polygonNum = polygonNum;
    }

    @Basic
    @Column(name = "type", nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "task_tag", joinColumns = {@JoinColumn(referencedColumnName = "ID")}
            , inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})
    @JsonIgnore
    public List<TagEntity> getTagEntityList() {
        return tagEntityList;
    }

    public void setTagEntityList(List<TagEntity> tagEntityList) {
        this.tagEntityList = tagEntityList;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "img_task", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    public List<ImageEntity> getImageEntityList() {
        return imageEntityList;
    }

    public void setImageEntityList(List<ImageEntity> imageEntityList) {
        this.imageEntityList = imageEntityList;
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TaskEntity that = (TaskEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sponsor, that.sponsor) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(workerLevel, that.workerLevel) &&
                Objects.equals(state, that.state) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(imageNum, that.imageNum) &&
                Objects.equals(imageEntityList, that.imageEntityList) &&
                Objects.equals(tagEntityList, that.tagEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sponsor, taskName, description, workerLevel, state, startDate, endDate, imageNum, imageEntityList, tagEntityList);
    }
}
