package cn.joker.dao;

import cn.joker.entity.BonusHistory;
import cn.joker.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
        JsonArray bonusArray = json.getAsJsonArray("bonusHistories");
        StringBuilder newJson = new StringBuilder("{\"bonusHistories\":[");
        for (Object o : bonusArray) {
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

    public List<BonusHistory> findByName(String username) {
        JsonObject json = Json.openJson("json/bonusHistory.json");
        assert json != null;
        List<BonusHistory> bonusHistories = new ArrayList<>();
        JsonArray userArray = json.getAsJsonArray("bonusHistories");
        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (Json.format(String.valueOf(object.get("workerName"))).equals(username)) {
                BonusHistory bonusHistory = new BonusHistory();
                bonusHistory.setWorkerName(username);
                bonusHistory.setPoints(Integer.valueOf(String.valueOf(object.get("points"))));
                bonusHistory.setTaskID(Integer.valueOf(String.valueOf(object.get("taskID"))));
                bonusHistories.add(bonusHistory);
            }
        }
        return bonusHistories;

    }
}
