package cn.joker66.dao;

import cn.joker66.entity.ImgMark;
import cn.joker66.util.Json;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 9:21 2018/4/14
 */
@Repository
public class ImgMarkDao {

    /**
    *@author:pis
    *@description: 更新标记
    *@date: 10:43 2018/4/14
    */
    public boolean refreshMark(ImgMark imgMark, JSONObject jsonObject) {
        StringBuilder newJson = new StringBuilder(jsonObject.toString());
        String name = "task/" + imgMark.getTaskID() + "/" + imgMark.getImgName() + "/" + imgMark.getWorkerName() + ".json";
        return Json.modifyJson(newJson,name);
    }

    /**
    *@author:pis
    *@description: 找到某一任务对应用户对应图片的所有标记
    *@date: 10:44 2018/4/14
    */
    public List<ImgMark> findAllMarks(JSONObject jsonObject) {
        Integer taskID =  jsonObject.getInt("taskID");
        JSONArray users = jsonObject.getJSONArray("users");
        String imgName = jsonObject.getString("imgName");
        List<ImgMark> list = new ArrayList<>();
        for(Object o : users){
            String name = "task/" + taskID.toString() + "/" + imgName + "/";
            JSONObject jsonObject1 = (JSONObject) o;
            String username = jsonObject1.getString("username");
            name += username + ".json";
            JsonObject markJson = Json.openJson(name);
            ImgMark imgMark = new ImgMark();
            imgMark.setTaskID(Integer.valueOf(String.valueOf(markJson.get("taskID"))));
            imgMark.setWorkerName(Json.format(markJson.get("workerName").toString()));
            imgMark.setSponsorName(Json.format(markJson.get("sponsorName").toString()));
            imgMark.setImgURL(Json.format(markJson.get("imgURL").toString()));
            imgMark.setNotePolygon(markJson.get("notePolygon").toString());
            imgMark.setNoteRectangle(markJson.get("noteRectangle").toString());
            imgMark.setNoteTotal(markJson.get("noteTotal").toString());
            list.add(imgMark);

        }
        return list;
    }
}
