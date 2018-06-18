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
@Table(name = "img_mark", schema = "imgannotator", catalog = "")
public class ImgMarkEntity implements Serializable {
    private Integer id;
    private ImageEntity image_imgMark;
    private UserEntity worker;
    private String noteRectangle;
    private String notePolygon;
    private String noteTotal;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "image_id")
    @JsonIgnore
    public ImageEntity getImage_imgMark() {
        return image_imgMark;
    }

    public void setImage_imgMark(ImageEntity image_imgMark) {
        this.image_imgMark = image_imgMark;
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


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public UserEntity getWorker() {
        return worker;
    }

    public void setWorker(UserEntity worker) {
        this.worker = worker;
    }

    @Basic
    @Column(name = "noteRectangle", nullable = true)
    public String getNoteRectangle() {
        return noteRectangle;
    }

    public void setNoteRectangle(String noteRectangle) {
        this.noteRectangle = noteRectangle;
    }

    @Basic
    @Column(name = "notePolygon", nullable = true)
    public String getNotePolygon() {
        return notePolygon;
    }

    public void setNotePolygon(String notePolygon) {
        this.notePolygon = notePolygon;
    }

    @Basic
    @Column(name = "noteTotal", nullable = true, length = 200)
    public String getNoteTotal() {
        return noteTotal;
    }

    public void setNoteTotal(String noteTotal) {
        this.noteTotal = noteTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImgMarkEntity that = (ImgMarkEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(image_imgMark, that.image_imgMark) &&
                Objects.equals(worker, that.worker) &&
                Objects.equals(noteRectangle, that.noteRectangle) &&
                Objects.equals(notePolygon, that.notePolygon) &&
                Objects.equals(noteTotal, that.noteTotal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, image_imgMark, worker, noteRectangle, notePolygon, noteTotal);
    }
}
