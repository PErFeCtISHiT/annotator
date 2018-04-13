package cn.joker66.entity;

/**
 * 举报信息的实体类
 */
public class ReportMessage {
    private String respondent;//被举报人的用户名，唯一识别
    private String reporter;//举报者的用户名，唯一识别
    private Integer taskID;//触发举报的任务ID
    private String taskName;//触发举报的任务名字
    private String description;//举报具体内容
    //相当于是举报信息的key
    private String reportTime;//举报时间
    private boolean isDealt;//管理员是否处理过举报信息
    //需要需要，不然后面没法分开处理了
    private Integer type;//举报类型 1为发起者举报工人 2为工人举报发起者

    public void setDealt(boolean dealt) {
        isDealt = dealt;
    }

    public void setRespondent(String respondent) {
        this.respondent = respondent;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isDealt() {
        return isDealt;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
