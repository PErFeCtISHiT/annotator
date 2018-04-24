package cn.joker.dao;

import cn.joker.entity.ReportMessage;
import cn.joker.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportDao {
    private String globalRespondent = "respondent";
    private String globalReporter = "reporter";
    private String globalTaskID = "taskID";
    private String globalTaskName = "taskName";
    private String globalDescription = "description";
    private String globalReportTime = "reportTime";
    private String globalIsDealt = "isDealt";

    public boolean reportWorker(ReportMessage reportMessage) {
        JsonObject jsonObject = JsonHelper.openJson("json/workerReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("workerReportedMessages");

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder("{\"workerReportedMessages\":[");

        newJson = addOneMessage(newJson, reportMessage, reportMessageArray);

        return JsonHelper.modifyJson(newJson, "json/workerReported.json");
    }

    public boolean reportTask(ReportMessage reportMessage) {
        JsonObject jsonObject = JsonHelper.openJson("json/taskReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("taskReportedMessages");

        //把已经保存的先拉取出来
        StringBuilder newJson = new StringBuilder("{\"taskReportedMessages\":[");

        newJson = addOneMessage(newJson, reportMessage, reportMessageArray);

        return JsonHelper.modifyJson(newJson, "json/taskReported.json");
    }

    /**
     * 查看举报工人的记录，工人被举报
     *
     * @return 举报信息列表
     */
    public List<ReportMessage> checkWorkerReport() {
        JsonObject jsonObject = JsonHelper.openJson("json/workerReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("workerReportedMessages");

        return convertJsonArrayToArrayList(reportMessageArray, 1);
    }

    /**
     * 查看举报任务的记录，任务被举报
     *
     * @return 举报信息列表
     */
    public List<ReportMessage> checkTaskReport() {
        JsonObject jsonObject = JsonHelper.openJson("json/taskReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("taskReportedMessages");

        return convertJsonArrayToArrayList(reportMessageArray, 2);
    }

    /**
     * 处理举报信息
     *
     * @param reportTime  举报时间，key
     * @param type        举报类型 1为查看用户被举报，2为查看任务被举报
     * @param description 对举报信息的处理描述信息
     * @return 是否成功修改信息
     */
    public boolean dealReport(String reportTime, Integer type, String description) {
        JsonObject jsonObject = JsonHelper.openJson("json/workerReported.json");
        assert jsonObject != null;
        JsonArray reportMessageArray = jsonObject.getAsJsonArray("workerReportedMessages");

        StringBuilder newJson = new StringBuilder();
        if (type == 1) {
            newJson.append("{\"workerReportedMessages\":[");
        } else {
            newJson.append("{\"taskReportedMessages\":[");
            jsonObject = JsonHelper.openJson("json/taskReported.json");
            reportMessageArray = jsonObject.getAsJsonArray("taskReportedMessages");
        }
        for (Object o : reportMessageArray) {
            JSONObject object = new JSONObject(o.toString());

            if (object.get(globalReportTime).toString().equals(reportTime)) {
                object.put(globalIsDealt, true);
                object.put(globalDescription, description);
            }
            newJson.append(object.toString());
            newJson.append(",");
        }

        newJson.append("]}");
        if (type == 1)
            return JsonHelper.modifyJson(newJson, "json/workerReported.json");
        else
            return JsonHelper.modifyJson(newJson, "json/taskReported.json");
    }

    /**
     * @param reportMessageArray json中的数组
     * @param i                  表示查看类型，1为查看用户被举报，2为查看任务被举报，与reportMessage中type属性相同
     * @return
     */
    private List<ReportMessage> convertJsonArrayToArrayList(JsonArray reportMessageArray, int i) {
        List<ReportMessage> workerReport = new ArrayList<>();
        for (Object o : reportMessageArray) {
            JSONObject object = new JSONObject(o.toString());
            //对尚未处理过的信息进行处理
            if (!object.getBoolean(globalIsDealt)) {
                ReportMessage reportMessage = new ReportMessage();
                reportMessage.setDescription(object.get(globalDescription).toString());
                reportMessage.setTaskName(object.get(globalTaskName).toString());
                reportMessage.setReporter(object.get(globalReporter).toString());
                reportMessage.setRespondent(object.get(globalRespondent).toString());
                reportMessage.setTaskID(object.getInt(globalTaskID));
                reportMessage.setReportTime(object.get(globalReportTime).toString());
                reportMessage.setType(i);

                workerReport.add(reportMessage);
            }
        }

        return workerReport;
    }

    private StringBuilder addOneMessage(StringBuilder newJson, ReportMessage reportMessage, JsonArray jsonArray) {
        for (Object o : jsonArray) {
            JsonObject object = (JsonObject) o;
            newJson.append(object.toString());
            newJson.append(",");
        }

        JSONObject pre = new JSONObject();
        pre.put(globalRespondent, reportMessage.getRespondent());
        pre.put(globalReporter, reportMessage.getReporter());
        pre.put(globalDescription, reportMessage.getDescription());
        pre.put(globalTaskID, reportMessage.getTaskID());
        pre.put(globalTaskName, reportMessage.getTaskName());
        pre.put(globalReportTime, reportMessage.getReportTime());
        pre.put(globalIsDealt, reportMessage.isDealt());

        newJson.append(pre.toString());
        newJson.append("]}");

        return newJson;
    }


}
