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
@Table(name = "image", schema = "imgannotator", catalog = "")
public class ImageEntity implements Serializable {
    private Integer id;
    private String url;
    private TaskEntity img_task;
    private String imgName;


    @Basic
    @Column(name = "imgName", nullable = false, length = 200)
    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    public TaskEntity getImg_task() {
        return img_task;
    }

    public void setImg_task(TaskEntity img_task) {
        this.img_task = img_task;
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
    @Column(name = "url", nullable = false, length = 200)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(url, that.url) &&
                Objects.equals(img_task, that.img_task);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, url, img_task);
    }
}
