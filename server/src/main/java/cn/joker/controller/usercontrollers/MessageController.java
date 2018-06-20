package cn.joker.controller.usercontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.StdName;
import cn.joker.sevice.*;
import cn.joker.util.JsonHelper;
import cn.joker.util.PasswordHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 19:51 2018/4/11
 */
@RestController
@RequestMapping("/user")
public class MessageController {
    @Resource
    private UserService userService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private TaskService taskService;
    @Resource
    private BonusHistoryService bonusHistoryService;
    @Resource
    private WorkerMatrixService workerMatrixService;
    @Resource
    private TagService tagService;


    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 注册，point，uid，state初始化为0
     * @date: 14:10 2018/4/8
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void signUp(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        UserEntity userInfo = new UserEntity();
        userInfo.setPoints(100);
        userInfo.setUsername(jsonObject.getString(StdName.USERNAME));
        userInfo.setWorkerMatrixEntities(new ArrayList<>());
        JSONArray jsonArray = jsonObject.getJSONArray(StdName.ROLELIST);
        List<SysRoleEntity> roleList = new ArrayList<>();
        userInfo.setPasswr(jsonObject.getString(StdName.PASSWR));
        PasswordHelper.encryptPassword(userInfo);
        userService.add(userInfo);
        for (Object obj : jsonArray) {
            Integer srid = (Integer) obj;
            roleList.add((SysRoleEntity) sysRoleService.findByID(srid));
            if (srid == 4) {
                WorkerMatrixEntity workerMatrixEntity;
                for (int i = 0; i < 5; i++) {
                    workerMatrixEntity = new WorkerMatrixEntity(userInfo, jsonObject.getDouble(StdName.RATE), jsonObject.getInt(StdName.NUM));
                    workerMatrixEntity.setUser_matrix(userInfo);
                    workerMatrixEntity.setNum(0.0);
                    workerMatrixService.add(workerMatrixEntity);
                    userInfo.getWorkerMatrixEntities().add(workerMatrixEntity);
                }
            }
        }
        userInfo.setType1Num(0);
        userInfo.setType2Num(0);
        userInfo.setType3Num(0);
        userInfo.setRoleEntityList(roleList);
        userInfo.setLev(1);
        userInfo.setNickname(jsonObject.getString(StdName.NICKNAME));
        userInfo.setState(1);
        userInfo.setBonus(0);
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, userService.modify(userInfo));
        JsonHelper.jsonToResponse(response, ret);
    }


    /**
     * @param userEntity 请求内容
     * @param response   返回内容
     * @author:pis
     * @description: 修改一个用户的信息
     * @date: 13:36 2018/4/13
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public void modifyMessage(@RequestBody UserEntity userEntity, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        UserEntity newEntity = userService.findByUsername(userEntity.getUsername());
        if (userEntity.getNickname() != null)
            newEntity.setNickname(userEntity.getNickname());
        if (userEntity.getPasswr() != null)
            newEntity.setPasswr(userEntity.getPasswr());
        PasswordHelper.encryptPassword(newEntity);
        ret.put(StdName.MES, userService.modify(newEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 按用户名查找一个用户是否存在
     * @date: 13:36 2018/4/13
     */
    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public void findUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];

        JSONObject ret = new JSONObject();
        if (userService.findByUsername(username) != null) {
            ret.put(StdName.EXISTED, true);

        } else {
            ret.put(StdName.EXISTED, false);
        }
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看所有用户
     * @date: 14:16 2018/4/13
     */

    @RequestMapping(value = "/checkUser", method = RequestMethod.GET)
    public List<UserEntity> checkUser() {
        return userService.findAll();

    }


    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 删除用户
     * @date: 14:16 2018/4/13
     */

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        JSONObject ret = new JSONObject();
        UserEntity userEntity = userService.findByUsername(username);
        ret.put(StdName.MES, userService.delete(userEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 积分提现充值
     * @date: 14:41 2018/4/13
     */

    @RequestMapping(value = "/changePoints", method = RequestMethod.POST)
    public void changePoints(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(StdName.USERNAME);
        Integer points = jsonObject.getInt(StdName.POINTS);
        UserEntity userEntity = userService.findByUsername(username);
        JSONObject ret = new JSONObject();
        if (userEntity != null) {
            System.out.println(points);
            userEntity.setPoints(userEntity.getPoints() + points);
            ret.put(StdName.MES, userService.modify(userEntity));
        } else
            ret.put(StdName.MES, StdName.NULL);
        JsonHelper.jsonToResponse(response, ret);

    }

    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 修改积分
     * @date: 14:45 2018/4/13
     */

    @RequestMapping(value = "/managePoints", method = RequestMethod.POST)
    public void managePoints(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(StdName.USERNAME);
        Integer points = jsonObject.getInt(StdName.POINTS);
        UserEntity userEntity = userService.findByUsername(username);
        JSONObject ret = new JSONObject();
        if (userEntity != null) {
            userEntity.setPoints(points);
            ret.put(StdName.MES, userService.modify(userEntity));
        } else
            ret.put(StdName.MES, StdName.NULL);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 奖励积分
     * @date: 14:45 2018/4/13
     */

    @RequestMapping(value = "/Bonus", method = RequestMethod.POST)
    public void bonus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        BonusHistoryEntity bonusHistory = new BonusHistoryEntity();
        bonusHistory.setPoints(jsonObject.getInt(StdName.POINTS));
        bonusHistory.setBonusHistory_task((TaskEntity) taskService.findByID(jsonObject.getInt(StdName.TASKID)));
        UserEntity userEntity = userService.findByUsername(jsonObject.getString(StdName.WORKERNAME));
        JSONObject ret = new JSONObject();
        if (userEntity != null) {
            bonusHistory.setBonusHistory_user(userEntity);
            userEntity.setPoints(userEntity.getPoints() + bonusHistory.getPoints());
            userEntity.setBonus(userEntity.getBonus() + bonusHistory.getPoints());
            userEntity.setLev((int) (Math.log(userEntity.getBonus() + 1.0) / Math.log(10)) + 1);
            ret.put(StdName.MES, bonusHistoryService.add(bonusHistory) && userService.modify(userEntity));
        } else
            ret.put(StdName.MES, StdName.NULL);
        JsonHelper.jsonToResponse(response, ret);

    }

    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 查看正确率是否达标
     * @date: 16:54 2018/6/16
     */
    @RequestMapping(value = "/checkAccuracy", method = RequestMethod.GET)
    public void checkAccuracy(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(StdName.USERNAME)[0];
        UserEntity userEntity = userService.findByUsername(username);
        String tag = map.get(StdName.TAG)[0];
        TagEntity tagEntity = tagService.findByTag(tag);
        JSONObject ret = new JSONObject();
        if (userEntity.getWorkerMatrixEntities().get(tagEntity.getId() - 1).getCorrect() >= 0.7)
            ret.put(StdName.MES, true);
        else
            ret.put(StdName.MES, false);
        JsonHelper.jsonToResponse(response, ret);
    }
}
