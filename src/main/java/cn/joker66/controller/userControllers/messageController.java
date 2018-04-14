package cn.joker66.controller.userControllers;

import cn.joker66.entity.BonusHistory;
import cn.joker66.entity.SysRole;
import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.BonusHistoryService;
import cn.joker66.sevice.SysRoleService;
import cn.joker66.sevice.UserInfoService;
import cn.joker66.util.Json;
import cn.joker66.util.PasswordHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * @date: create in 19:51 2018/4/11
 */
@Controller
@RequestMapping("/user")
public class messageController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private BonusHistoryService bonusHistoryService;

    /**
     * @author:pis
     * @description: 注册，point，uid，state初始化为0
     * @date: 14:10 2018/4/8
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @RequiresPermissions("signUp")
    public void signUp(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = Json.requestToJson(request);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(1);
        userInfo.setPoints(0);
        userInfo.setUsername((String) jsonObject.get("username"));

        JSONArray jsonArray = jsonObject.getJSONArray("roleList");
        List<SysRole> roleList = new ArrayList<>();
        for (Object obj : jsonArray) {
            String srid = String.valueOf(obj);
            roleList.add(sysRoleService.findBySysRoleId(srid));
        }
        userInfo.setRoleList(roleList);
        userInfo.setPassword((String) jsonObject.get("password"));
        PasswordHelper.encryptPassword(userInfo);
        userInfo.setLevel(1);
        userInfo.setName((String) jsonObject.get("username"));
        userInfo.setState(1);
        JSONObject ret = new JSONObject();

        ret.put("mes", userInfoService.addUser(userInfo));
        Json.JsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 修改一个用户的信息
     * @date: 13:36 2018/4/13
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @RequiresPermissions("submitAssignment")
    public void modifyMessage(HttpServletRequest request, HttpServletResponse response) {
        JSONObject newUser = Json.requestToJson(request);
        UserInfo newUserInfo = userInfoService.findByUsername((String) newUser.get("username"));
        newUserInfo.setPassword((String) newUser.get("password"));
        newUserInfo.setName((String) newUser.get("name"));
        JSONArray jsonArray = newUser.getJSONArray("roleList");
        List<SysRole> roleList = new ArrayList<>();
        for (Object obj : jsonArray) {
            String srid = String.valueOf(obj);
            roleList.add(sysRoleService.findBySysRoleId(srid));
        }
        newUserInfo.setRoleList(roleList);
        PasswordHelper.encryptPassword(newUserInfo);

        JSONObject ret = new JSONObject();

        ret.put("mes", userInfoService.modifyUser(newUserInfo));
        Json.JsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 按用户名查找一个用户是否存在
     * @date: 13:36 2018/4/13
     */
    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public void findUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get("username")[0];

        JSONObject ret = new JSONObject();
        if (userInfoService.findByUsername(username) != null) {
            ret.put("existed", true);

        } else {
            ret.put("existed", false);
        }
        Json.JsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看所有用户
     * @date: 14:16 2018/4/13
     */

    @RequestMapping(value = "/checkUser", method = RequestMethod.GET)
    public void checkUser(HttpServletResponse response) {
        List<UserInfo> userInfos = userInfoService.findAllUser();
        JSONObject ret = new JSONObject();
        JSONArray userInfoArray = new JSONArray();
        for (UserInfo userInfo : userInfos) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", userInfo.getUsername());
            jsonObject.put("name", userInfo.getName());
            List<SysRole> list = userInfo.getRoleList();
            JSONArray list1 = new JSONArray();
            for (SysRole sysRole : list) {
                list1.put(sysRole.getSrid());
            }
            jsonObject.put("role", list1);
            userInfoArray.put(jsonObject);
        }
        ret.put("users", userInfoArray);
        Json.JsonToResponse(response, ret);

    }


    /**
     * @author:pis
     * @description: 删除用户
     * @date: 14:16 2018/4/13
     */

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get("username")[0];
        JSONObject ret = new JSONObject();

        ret.put("mes", userInfoService.deleteUser(username));
        Json.JsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 积分提现充值
     * @date: 14:41 2018/4/13
     */

    @RequestMapping(value = "/changePoints", method = RequestMethod.POST)
    public void changePoints(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = Json.requestToJson(request);
        String username = (String) jsonObject.get("username");
        Integer points = (Integer) jsonObject.get("points");
        UserInfo userInfo = userInfoService.findByUsername(username);
        userInfo.setPoints(userInfo.getPoints() + points);
        JSONObject ret = new JSONObject();
        ret.put("mes", userInfoService.modifyUser(userInfo));
        Json.JsonToResponse(response, ret);

    }

    /**
     * @author:pis
     * @description: 修改积分
     * @date: 14:45 2018/4/13
     */

    @RequestMapping(value = "/managePoints", method = RequestMethod.POST)
    public void managePoints(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = Json.requestToJson(request);
        String username = (String) jsonObject.get("username");
        Integer points = (Integer) jsonObject.get("points");
        UserInfo userInfo = userInfoService.findByUsername(username);
        userInfo.setPoints(points);
        JSONObject ret = new JSONObject();
        ret.put("mes", userInfoService.modifyUser(userInfo));
        Json.JsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 奖励积分
     * @date: 14:45 2018/4/13
     */

    @RequestMapping(value = "/Bonus", method = RequestMethod.POST)
    public void Bonus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = Json.requestToJson(request);
        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.setPoints((Integer) jsonObject.get("points"));
        bonusHistory.setTaskID((Integer) jsonObject.get("taskID"));
        bonusHistory.setWorkerName((String) jsonObject.get("workerName"));

        UserInfo userInfo = userInfoService.findByUsername(bonusHistory.getWorkerName());
        userInfo.setPoints(userInfo.getPoints() + bonusHistory.getPoints());

        JSONObject ret = new JSONObject();

        ret.put("mes", bonusHistoryService.addBonusHistory(bonusHistory) && userInfoService.modifyUser(userInfo));
        Json.JsonToResponse(response, ret);

    }
}
