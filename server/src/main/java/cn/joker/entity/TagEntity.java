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
@Table(name = "tag", schema = "imgannotator", catalog = "")
public class TagEntity implements Serializable {
    private Integer id;
    private String tag;
    private String description;
    private List<TaskEntity> taskEntityList;
    private List<ImageEntity> testImageList;
    private List<ImageEntity> testImageList1;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "image_tag1", joinColumns = {@JoinColumn(referencedColumnName = "ID")}
            , inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})
    @JsonIgnore
    public List<ImageEntity> getTestImageList1() {
        return testImageList1;
    }

    public void setTestImageList1(List<ImageEntity> testImageList1) {
        this.testImageList1 = testImageList1;
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "image_tag", joinColumns = {@JoinColumn(referencedColumnName = "ID")}
            , inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})
    @JsonIgnore
    public List<ImageEntity> getTestImageList() {
        return testImageList;
    }

    public void setTestImageList(List<ImageEntity> testImageList) {
        this.testImageList = testImageList;
    }


    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "tagEntityList",fetch = FetchType.EAGER)
    @JsonIgnore
    public List<TaskEntity> getTaskEntityList() {
        return taskEntityList;
    }

    public void setTaskEntityList(List<TaskEntity> taskEntityList) {
        this.taskEntityList = taskEntityList;
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
    @Column(name = "tag", nullable = false, length = 200)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TagEntity tagEntity = (TagEntity) o;
        return Objects.equals(id, tagEntity.id) &&
                Objects.equals(tag, tagEntity.tag) &&
                Objects.equals(description, tagEntity.description) &&
                Objects.equals(taskEntityList, tagEntity.taskEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, tag, description, taskEntityList);
    }
}
