package cn.joker66.dao;

import cn.joker66.entity.BonusHistory;
import cn.joker66.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:58 2018/4/13
 */
@Repository
public class BonusHistoryDao {
    public boolean addBonusHistory(BonusHistory bonusHistory) {
        JsonObject json = Json.openJson("json/bonusHistory.json");
        assert json != null;
        JsonArray userArray = json.getAsJsonArray("bonusHistories");
        StringBuilder newJson = new StringBuilder("{\"bonusHistories\":[");
        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            newJson.append(object.toString());
            newJson.append(",");

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workerName", bonusHistory.getWorkerName());
        jsonObject.put("taskID", bonusHistory.getTaskID());
        jsonObject.put("points", bonusHistory.getPoints());
        newJson.append(jsonObject.toString());
        newJson.append("]}");
        return this.updateJson(newJson);
    }

    private boolean updateJson(StringBuilder newJson) {
        return Json.modifyJson(newJson, "json/bonusHistory.json");
    }
}
