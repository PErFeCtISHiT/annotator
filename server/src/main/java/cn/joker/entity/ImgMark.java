package cn.joker.entity;

import java.io.Serializable;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:19 2018/4/13
 */
public class ImgMark implements Serializable {
    private String imgName;
    private String workerName;
    private String sponsorName;
    private String imgURL;
    private String noteRectangle;
    private String notePolygon;
    private String noteTotal;
    private Integer taskID;

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getNoteRectangle() {
        return noteRectangle;
    }

    public void setNoteRectangle(String noteRectangle) {
        this.noteRectangle = noteRectangle;
    }

    public String getNotePolygon() {
        return notePolygon;
    }

    public void setNotePolygon(String notePolygon) {
        this.notePolygon = notePolygon;
    }

    public String getNoteTotal() {
        return noteTotal;
    }

    public void setNoteTotal(String noteTotal) {
        this.noteTotal = noteTotal;
    }
}
