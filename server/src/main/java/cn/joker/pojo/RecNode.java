package cn.joker.pojo;

import cn.joker.entity.UserEntity;

public class RecNode {
    private double top;
    private double left;
    private double height;
    private double width;
    private String mark;
    private UserEntity worker;

    public RecNode(double top, double left, double height, double width, String mark,UserEntity worker){
        this.top = top;
        this.left = left;
        this.height = height;
        this.width = width;
        this.mark = mark;
        this.worker = worker;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public UserEntity getWorker() {
        return worker;
    }

    public void setWorker(UserEntity worker) {
        this.worker = worker;
    }
}
