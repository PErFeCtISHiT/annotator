package cn.joker66.dao;

import cn.joker66.entity.ReportMessage;
import cn.joker66.util.Json;
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
        JsonObject jsonObject = Json.openJson("/workerReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("workerReportedMessages");

        return convertJsonArrayToArrayList(reportMessageArray, 1);
    }

    /**
     * 查看举报任务的记录，任务被举报
     * @return
     */
    public List<ReportMessage> checkTaskReport() {
        JsonObject jsonObject = Json.openJson("/taskReported.json");
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
            if(Json.format(object.get("isDealt").toString()).equals(false)){
                ReportMessage reportMessage = new ReportMessage();
                reportMessage.setDescription(Json.format(object.get("description").toString()));
                reportMessage.setTaskName(Json.format(object.get("taskName").toString()));
                reportMessage.setReporter(Json.format(object.get("reporter").toString()));
                reportMessage.setRespondent(Json.format(object.get("respondent").toString()));
                reportMessage.setTaskID(Integer.valueOf(Json.format(object.get("taskID").toString())));
                reportMessage.setReportTime(Json.format(object.get("reportTime").toString()));
                reportMessage.setType(i);

                workerReport.add(reportMessage);
            }
        }

        return workerReport;
    }
}
