package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:55 2018/5/4
 */
@Entity
@Table(name = "reportmessage", schema = "imgannotator", catalog = "")
public class ReportmessageEntity implements Serializable {
    private Integer id;
    private UserEntity respondent;
    private UserEntity reporter;
    private TaskEntity task;
    private String description;
    private Date reportTime;
    private Byte isDealt;
    private Integer type;

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
    @JoinColumn(name = "respondent_id")
    @JsonIgnore
    public UserEntity getRespondent() {
        return respondent;
    }

    public void setRespondent(UserEntity respondent) {
        this.respondent = respondent;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reporter_id")
    @JsonIgnore
    public UserEntity getReporter() {
        return reporter;
    }

    public void setReporter(UserEntity reporter) {
        this.reporter = reporter;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
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
    @Column(name = "reportTime", nullable = true)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    @Basic
    @Column(name = "isDealt", nullable = true)
    public Byte getIsDealt() {
        return isDealt;
    }

    public void setIsDealt(Byte isDealt) {
        this.isDealt = isDealt;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ReportmessageEntity that = (ReportmessageEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(respondent, that.respondent) &&
                Objects.equals(reporter, that.reporter) &&
                Objects.equals(task, that.task) &&
                Objects.equals(description, that.description) &&
                Objects.equals(reportTime, that.reportTime) &&
                Objects.equals(isDealt, that.isDealt) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, respondent, reporter, task, description, reportTime, isDealt, type);
    }
}
