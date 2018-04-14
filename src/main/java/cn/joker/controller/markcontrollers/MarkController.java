package cn.joker.controller.markcontrollers;

import cn.joker.entity.ImgMark;
import cn.joker.sevice.ImgMarkService;
import cn.joker.util.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/4/13
 */
@Controller
@RequestMapping("/mark")
public class MarkController {
    @Resource
    private ImgMarkService imgMarkService;

    /**
     * @author:pis
     * @description: 上传图片标注
     * @date: 9:06 2018/4/14
     */
    @RequestMapping(value = "/postMark", method = RequestMethod.POST)
    public void postMark(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = Json.requestToJson(request);
        ImgMark imgMark = new ImgMark();
        imgMark.setImgURL((String) jsonObject.get("imgURL"));
        imgMark.setImgName(imgMark.getImgURL().substring(imgMark.getImgURL().lastIndexOf('/') + 1));
        imgMark.setSponsorName((String) jsonObject.get("sponsorName"));
        imgMark.setWorkerName((String) jsonObject.get("workerName"));
        imgMark.setTaskID((Integer) jsonObject.get("taskID"));
        JSONObject ret = new JSONObject();
        ret.put("mes", imgMarkService.refreshMark(imgMark, jsonObject));
        Json.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看图片标注
     * @date: 10:33 2018/4/14
     */
    @RequestMapping(value = "checkImage", method = RequestMethod.POST)
    public void checkMark(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = Json.requestToJson(request);
        JSONObject ret = new JSONObject();
        JSONArray imgMarkArray = new JSONArray();
        List<ImgMark> imgMarks = imgMarkService.findAllMarks(jsonObject);
        for (ImgMark imgMark : imgMarks) {
            JSONObject imgMarkJson = new JSONObject();
            imgMarkJson.put("imgURL", imgMark.getImgURL());
            imgMarkJson.put("workerName", imgMark.getWorkerName());
            imgMarkJson.put("sponsorName", imgMark.getSponsorName());
            JSONArray jsonArray = new JSONArray(imgMark.getNotePolygon());
            imgMarkJson.put("notePolygon", jsonArray);
            jsonArray = new JSONArray(imgMark.getNoteRectangle());
            imgMarkJson.put("noteRectangle", jsonArray);
            jsonArray = new JSONArray(imgMark.getNoteTotal());
            imgMarkJson.put("noteTotal", jsonArray);
            imgMarkJson.put("taskID", imgMark.getTaskID());
            imgMarkArray.put(imgMarkJson);
        }
        ret.put("marks", imgMarkArray);
        Json.jsonToResponse(response, ret);
    }
}
