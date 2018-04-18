package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.BonusHistory;
import cn.joker.entity.SysRole;
import cn.joker.entity.UserInfo;
import cn.joker.sevice.BonusHistoryService;
import cn.joker.sevice.UserInfoService;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:59 2018/4/15
 */
@Controller
@RequestMapping("/statistic")
public class WorkerController {
    @Resource
    private BonusHistoryService bonusHistoryService;
    @Resource
    private UserInfoService userInfoService;


    /**
     * @author:pis
     * @description: 查看某个工人的奖励历史
     * @date: 16:05 2018/4/15
     */
    @RequestMapping(value = "/checkBonus", method = RequestMethod.GET)
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
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看群体排名
     * @date: 18:56 2018/4/17
     */
    @RequestMapping(value = "/checkRanking", method = RequestMethod.GET)
    public void checkRanking(HttpServletResponse response) {
        List<UserInfo> userInfos = userInfoService.findAllUser();
        JSONArray workersArray = new JSONArray();
        List<UserInfo> newUserInfos = new ArrayList<>();
        for (UserInfo userInfo : userInfos) {
            List<SysRole> sysRoles = userInfo.getRoleList();
            int tag = 0;
            for (SysRole sysRole : sysRoles) {
                if (sysRole.getSrid() == 3)
                    tag = 1;
            }
            if (tag == 1) {
                newUserInfos.add(userInfo);
            }
        }
        newUserInfos.sort((o1, o2) -> o2.getBonus() - o1.getBonus());
        for (int i = 0; i < newUserInfos.size(); i++) {
            UserInfo userInfo = newUserInfos.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", userInfo.getUsername());
            jsonObject.put("name", userInfo.getName());
            jsonObject.put("rank", i + 1);
            jsonObject.put("points", userInfo.getPoints());
            workersArray.put(jsonObject);
        }
        JSONObject ret = new JSONObject();
        ret.put("workers", workersArray);
        JsonHelper.jsonToResponse(response, ret);
    }
}
