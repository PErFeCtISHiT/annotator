package cn.joker66.entity;

import java.util.Date;

/**
 * 举报信息的实体类
 */
public class ReportMessage {
    private String respondent;//被举报人的用户名，唯一识别
    private String reporter;//举报者的用户名，唯一识别
    private Integer taskID;//触发举报的任务ID
    private String taskName;//触发举报的任务名字
    private String description;//举报具体内容
    private Date reportTime;//举报时间
    private boolean isDealt;//管理员是否处理过举报信息
    //待商定
    private Integer type;//举报类型 1为发起者举报工人 2为工人举报发起者
}
