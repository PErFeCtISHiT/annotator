package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.BonusHistory;
import cn.joker.sevice.BonusHistoryService;
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
import java.util.Map;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:59 2018/4/15
 */
@Controller
@RequestMapping("statistic")
public class workerController {
    @Resource
    private BonusHistoryService bonusHistoryService;


    /**
     * @author:pis
     * @description: 查看某个工人的奖励历史
     * @date: 16:05 2018/4/15
     */
    @RequestMapping(value = "checkBonus", method = RequestMethod.GET)
    public void checkBonus(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get("username")[0];
        List<BonusHistory> bonusHistories = bonusHistoryService.findByName(username);
        JSONObject ret = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (BonusHistory bonusHistory : bonusHistories) {
            JSONObject bonusObj = new JSONObject();
            bonusObj.put("taskID", bonusHistory.getTaskID());
            bonusObj.put("points", bonusHistory.getPoints());
            jsonArray.put(bonusObj);
        }
        ret.put("mes", jsonArray);
        Json.jsonToResponse(response, ret);
    }
}
