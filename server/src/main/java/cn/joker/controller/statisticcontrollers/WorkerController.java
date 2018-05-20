package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.BonusHistoryEntity;
import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.BonusHistoryService;
import cn.joker.sevice.UserService;
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
    private UserService userService;


    /**
     * @author:pis
     * @description: 查看某个工人的奖励历史
     * @date: 16:05 2018/4/15
     */
    @RequestMapping(value = "/checkBonus", method = RequestMethod.GET)
    public void checkBonus(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(stdName.USERNAME)[0];
        List<BonusHistoryEntity> bonusHistories = bonusHistoryService.findByName(username);
        JSONObject ret = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (BonusHistoryEntity bonusHistory : bonusHistories) {
            JSONObject bonusObj = new JSONObject();
            bonusObj.put(stdName.TASKID, bonusHistory.getBonusHistory_task().getId());
            bonusObj.put(stdName.POINTS, bonusHistory.getPoints());
            jsonArray.put(bonusObj);
        }
        ret.put(stdName.MES, jsonArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看群体排名
     * @date: 18:56 2018/4/17
     */
    @RequestMapping(value = "/checkRanking", method = RequestMethod.GET)
    public void checkRanking(HttpServletResponse response) {
        List<UserEntity> userInfos = userService.findAll();
        JSONArray workersArray = new JSONArray();
        List<UserEntity> newUserInfos = new ArrayList<>();
        for (UserEntity userInfo : userInfos) {
            List<SysRoleEntity> sysRoles = userInfo.getRoleEntityList();
            int tag = 0;
            for (SysRoleEntity sysRole : sysRoles) {
                if (sysRole.getId() == 3)
                    tag = 1;
            }
            if (tag == 1) {
                newUserInfos.add(userInfo);
            }
        }
        newUserInfos.sort((o1, o2) -> o2.getBonus() - o1.getBonus());
        for (int i = 0; i < newUserInfos.size(); i++) {
            UserEntity userInfo = newUserInfos.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(stdName.USERNAME, userInfo.getUsername());
            jsonObject.put(stdName.NICKNAME, userInfo.getNickname());
            jsonObject.put(stdName.RANK, i + 1);
            jsonObject.put(stdName.POINTS, userInfo.getPoints());
            workersArray.put(jsonObject);
        }
        JSONObject ret = new JSONObject();
        ret.put(stdName.WORKERS, workersArray);
        JsonHelper.jsonToResponse(response, ret);
    }
}
