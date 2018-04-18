package cn.joker.dao;

import cn.joker.entity.ReportMessage;
import cn.joker.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ReportDao {
    public boolean reportWorker(ReportMessage reportMessage) {
        //todo
        return false;
    }

    public boolean reportTask(ReportMessage reportMessage) {
        //todo
        return false;
    }

    /**
     * 查看举报工人的记录，工人被举报
     * @return
     */
    public List<ReportMessage> checkWorkerReport() {
        JsonObject jsonObject = JsonHelper.openJson("/workerReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("workerReportedMessages");

        return convertJsonArrayToArrayList(reportMessageArray, 1);
    }

    /**
     * 查看举报任务的记录，任务被举报
     * @return
     */
    public List<ReportMessage> checkTaskReport() {
        JsonObject jsonObject = JsonHelper.openJson("/taskReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("taskReportedMessages");

        return convertJsonArrayToArrayList(reportMessageArray, 2);
    }

    public boolean dealReport(String reportTime, Integer type, String description) {
        //todo
        return false;
    }

    /**
     *
     * @param reportMessageArray json中的数组
     * @param i 表示查看类型，1为查看用户被举报，2为查看任务被举报，与reportMessage中type属性相同
     * @return
     */
    private List<ReportMessage> convertJsonArrayToArrayList(JsonArray reportMessageArray, int i){
        List<ReportMessage> workerReport = new ArrayList<>();

        for(Object o : reportMessageArray){
            JsonObject object = (JsonObject) o;
            //对尚未处理过的信息进行处理
            if(JsonHelper.format(object.get("isDealt").toString()).equals(false)){
                ReportMessage reportMessage = new ReportMessage();
                reportMessage.setDescription(JsonHelper.format(object.get("description").toString()));
                reportMessage.setTaskName(JsonHelper.format(object.get("taskName").toString()));
                reportMessage.setReporter(JsonHelper.format(object.get("reporter").toString()));
                reportMessage.setRespondent(JsonHelper.format(object.get("respondent").toString()));
                reportMessage.setTaskID(Integer.valueOf(JsonHelper.format(object.get("taskID").toString())));
                reportMessage.setReportTime(JsonHelper.format(object.get("reportTime").toString()));
                reportMessage.setType(i);

                workerReport.add(reportMessage);
            }
        }

        return workerReport;
    }
}
