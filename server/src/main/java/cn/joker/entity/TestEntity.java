package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:55 2018/5/4
 */
@Entity
@Table(name = "test", schema = "imgannotator",catalog = "")
public class TestEntity implements Serializable{
    private Integer id;
    private String imgURL;
    private String description;
    private String testType;
    @JsonIgnore
    private String choices;
    private String answer;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    @Transient
    private Double faultTolerantRate;

    public void setFaultTolerantRate(Double faultTolerantRate) {
        this.faultTolerantRate = faultTolerantRate;
    }

    public Double getFaultTolerantRate() {
        return 0.05;
    }



    @Basic
    @Column(name = "x", nullable = true)
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }
    @Basic
    @Column(name = "y", nullable = true)
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
    @Basic
    @Column(name = "width", nullable = true)
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
    @Basic
    @Column(name = "height", nullable = true)
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "answer", nullable = false, length = 200)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name = "choices", nullable = true, length = 200)
    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    @Basic
    @Column(name = "test_type", nullable = false, length = 200)
    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
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
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Basic
    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
