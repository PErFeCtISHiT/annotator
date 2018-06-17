package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.BonusHistoryEntity;
import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.BonusHistoryService;
import cn.joker.sevice.UserService;
import cn.joker.statisticalmethod.Comentropy;
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
        String username = map.get(StdName.USERNAME)[0];
        List<BonusHistoryEntity> bonusHistories = bonusHistoryService.findByName(username);
        JSONObject ret = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (BonusHistoryEntity bonusHistory : bonusHistories) {
            JSONObject bonusObj = new JSONObject();
            bonusObj.put(StdName.TASKID, bonusHistory.getBonusHistory_task().getId());
            bonusObj.put(StdName.POINTS, bonusHistory.getPoints());
            jsonArray.put(bonusObj);
        }
        ret.put(StdName.MES, jsonArray);
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
            jsonObject.put(StdName.USERNAME, userInfo.getUsername());
            jsonObject.put(StdName.NICKNAME, userInfo.getNickname());
            jsonObject.put(StdName.RANK, i + 1);
            jsonObject.put(StdName.POINTS, userInfo.getPoints());
            workersArray.put(jsonObject);
        }
        JSONObject ret = new JSONObject();
        ret.put(StdName.WORKERS, workersArray);
        JsonHelper.jsonToResponse(response, ret);
    }
    /**
    *@author:pis
    *@description: 查看正确率
    *@date: 12:18 2018/6/17
    */
    @RequestMapping(value = "/getCorrect", method = RequestMethod.GET)
    public void getCorrect(HttpServletResponse response,HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        UserEntity userEntity = userService.findByUsername(username);
        List<UserEntity> workers = userService.findAllWorkers();
        Double [][]data = new Double[workers.size() + 1][5];
        for(int i = 0;i < workers.size();i++){
            for(int j = 0;j < 5;j++){
                data[i][j] = workers.get(i).getWorkerMatrixEntities().get(j).getCorrect();
            }
        }
        for(int j = 0;j < 5;j++){
            data[workers.size()][j] = userEntity.getWorkerMatrixEntities().get(j).getCorrect();
        }
        Double []correct = Comentropy.getComentropy(data);
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        for(int i = 0;i < 5;i++){
            array.put(userEntity.getWorkerMatrixEntities().get(i).getCorrect());
        }
        array.put(correct[workers.size()]);
        ret.put(StdName.MES,array);
        JsonHelper.jsonToResponse(response,ret);
    }
}
