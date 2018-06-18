package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.BonusHistoryEntity;
import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.BonusHistoryService;
import cn.joker.sevice.TaskService;
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
public class UserController {
    @Resource
    private BonusHistoryService bonusHistoryService;
    @Resource
    private UserService userService;
    @Resource
    private TaskService taskService;


    /**
     * @param request  请求内容
     * @param response 返回内容
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
     * @param response 返回内容
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
                if (sysRole.getId() == 4)
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
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 查看正确率
     * @date: 12:18 2018/6/17
     */
    @RequestMapping(value = "/getCorrect", method = RequestMethod.GET)
    public void getCorrect(HttpServletResponse response, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        UserEntity userEntity = userService.findByUsername(username);
        List<UserEntity> workers = userService.findAllWorkers();
        Double[][] data = new Double[workers.size() + 1][5];
        for (int i = 0; i < workers.size(); i++) {
            for (int j = 0; j < 5; j++) {
                data[i][j] = workers.get(i).getWorkerMatrixEntities().get(j).getCorrect();
            }
        }
        for (int j = 0; j < 5; j++) {
            data[workers.size()][j] = userEntity.getWorkerMatrixEntities().get(j).getCorrect();
        }
        Double[] correct = Comentropy.getComentropy(data);
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < 5; i++) {
            array.put(userEntity.getWorkerMatrixEntities().get(i).getCorrect());
        }
        array.put(correct[workers.size()]);
        ret.put(StdName.MES, array);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 查看标注数
     * @date: 13:53 2018/6/17
     */
    @RequestMapping(value = "/getMarkNum", method = RequestMethod.GET)
    public void getMarkNum(HttpServletResponse response, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        UserEntity userEntity = userService.findByUsername(username);
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(userEntity.getType1Num());
        array.put(userEntity.getType2Num());
        array.put(userEntity.getType3Num());
        for (int i = 0; i < 5; i++) {
            array.put(userEntity.getWorkerMatrixEntities().get(i).getNum());
        }
        ret.put(StdName.MES, array);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看任务数
     * @param request 请求内容
     * @param response 返回内容
     * @date: 14:07 2018/6/17
     */
    @RequestMapping(value = "/getTaskNum", method = RequestMethod.GET)
    public void getTaskNum(HttpServletResponse response, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        UserEntity userEntity = userService.findByUsername(username);
        List<TaskEntity> taskEntities = taskService.findAll();
        Integer complete = 0;
        Integer unComplete = 0;
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getSponsor().getId().equals(userEntity.getId())) {
                if (taskEntity.getState() == 1) {
                    unComplete++;
                } else
                    complete++;
            }
        }
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(complete);
        array.put(unComplete);
        ret.put(StdName.MES, array);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看系统发布任务情况
     * @param request 请求内容
     * @param response 返回内容
     * @date: 14:13 2018/6/17
     */
    @RequestMapping(value = "/getTaskDetail", method = RequestMethod.GET)
    public void getTaskDetail(HttpServletResponse response, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        UserEntity userEntity = userService.findByUsername(username);
        Integer type1 = 0;
        Integer type2 = 0;
        Integer type3 = 0;
        Integer userType1 = 0;
        Integer userType2 = 0;
        Integer userType3 = 0;
        List<TaskEntity> taskEntities = taskService.findAll();
        for (TaskEntity taskEntity : taskEntities) {
            switch (taskEntity.getType()) {
                case 1:
                    type1++;
                    if (taskEntity.getSponsor().getId().equals(userEntity.getId()))
                        userType1++;
                    break;
                case 2:
                    type2++;
                    if (taskEntity.getSponsor().getId().equals(userEntity.getId()))
                        userType2++;
                    break;
                case 3:
                    type3++;
                    if (taskEntity.getSponsor().getId().equals(userEntity.getId()))
                        userType3++;
                    break;
            }
        }
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(type1);
        array.put(type2);
        array.put(type3);
        array.put(userType1);
        array.put(userType2);
        array.put(userType3);
        ret.put(StdName.MES, array);
        JsonHelper.jsonToResponse(response, ret);
    }
}
