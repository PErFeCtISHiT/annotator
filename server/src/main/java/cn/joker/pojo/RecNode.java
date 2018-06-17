package cn.joker.pojo;

import cn.joker.entity.UserEntity;

/**
 * 在分析统计的时候要用到的类
 */
public class RecNode {
    private double top;
    private double left;
    private double height;
    private double width;
    private String mark;
    private UserEntity worker;

    /**
     *
     * @param top 上
     * @param left 左
     * @param height 高度
     * @param width 宽度
     * @param mark 标注
     * @param worker 工人
     */
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
