package cn.joker.controller.usercontrollers;

import cn.joker.dao.SysPermissionRepository;
import cn.joker.dao.SysRoleRepository;
import cn.joker.dao.UserRepository;
import cn.joker.entity.SysPermissionEntity;
import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.stdName;
import cn.joker.util.JsonHelper;
import cn.joker.util.PasswordHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 19:51 2018/4/11
 */
@Controller
@RequestMapping("/user")
public class MessageController {
    private final UserRepository userRepository;
    private final SysRoleRepository sysRoleRepository;
    private final SysPermissionRepository sysPermissionRepository;

    @Autowired
    public MessageController(UserRepository userRepository, SysRoleRepository sysRoleRepository, SysPermissionRepository sysPermissionRepository) {
        this.userRepository = userRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.sysPermissionRepository = sysPermissionRepository;
    }

    /**
     * @author:pis
     * @description: 注册，point，uid，state初始化为0
     * @date: 14:10 2018/4/8
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public void signUp(@RequestBody UserEntity userEntity, HttpServletResponse response) {
//        JSONObject jsonObject = JsonHelper.requestToJson(request);
//        UserEntity userInfo = new UserEntity();
//        userInfo.setPoints(100);
//        userInfo.setUsername((String) jsonObject.get(globalUsername));
//
//        JSONArray jsonArray = jsonObject.getJSONArray(globalRoleList);
//        List<SysRole> roleList = new ArrayList<>();
//        for (Object obj : jsonArray) {
//            String srid = String.valueOf(obj);
//            roleList.add(sysRoleService.findBySysRoleId(srid));
//        }
//        userInfo.setRoleList(roleList);
//        userInfo.setPassword((String) jsonObject.get(globalPasswr));
//        PasswordHelper.encryptPassword(userInfo);
//        userInfo.setLevel(1.0);
//        userInfo.setName((String) jsonObject.get(globalName));
//        userInfo.setState(1);
//        userInfo.setBonus(0);
//        PasswordHelper.encryptPassword(userEntity);
//        JSONObject ret = new JSONObject();
//
//        ret.put(stdName.RET, userRepository.saveAndFlush(userEntity));
//        JsonHelper.jsonToResponse(response, ret);
        SysRoleEntity sysRoleEntity = sysRoleRepository.findById(5);
        List<SysPermissionEntity> sysPermissionEntities = new ArrayList<>();
        sysPermissionEntities.add(sysPermissionRepository.findById(1));
        sysRoleEntity.setSysPermissionEntityList(sysPermissionEntities);
        sysRoleRepository.saveAndFlush(sysRoleEntity);
        sysRoleRepository.delete(sysRoleEntity);
    }

//    /**
//     * @author:pis
//     * @description: 修改一个用户的信息
//     * @date: 13:36 2018/4/13
//     */
//    @RequestMapping(value = "/modify", method = RequestMethod.POST)
//    @RequiresPermissions("submitAssignment")
//    public void modifyMessage(HttpServletRequest request, HttpServletResponse response) {
//        JSONObject newUser = JsonHelper.requestToJson(request);
//        UserInfo newUserInfo = userInfoService.findByUsername((String) newUser.get(globalUsername));
//        JSONObject ret = new JSONObject();
//        if (newUserInfo != null) {
//
//
//            newUserInfo.setPassword((String) newUser.get(globalPasswr));
//            newUserInfo.setName((String) newUser.get(globalName));
//            JSONArray jsonArray = newUser.getJSONArray(globalRoleList);
//            List<SysRole> roleList = new ArrayList<>();
//            for (Object obj : jsonArray) {
//                String srid = String.valueOf(obj);
//                roleList.add(sysRoleService.findBySysRoleId(srid));
//            }
//            newUserInfo.setRoleList(roleList);
//            PasswordHelper.encryptPassword(newUserInfo);
//
//
//            ret.put(globalMes, userInfoService.modifyUser(newUserInfo));
//        } else
//            ret.put("ret", "null");
//        JsonHelper.jsonToResponse(response, ret);
//    }
//
//    /**
//     * @author:pis
//     * @description: 按用户名查找一个用户是否存在
//     * @date: 13:36 2018/4/13
//     */
//    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
//    public void findUser(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, String[]> map = request.getParameterMap();
//        String username = map.get(globalUsername)[0];
//
//        JSONObject ret = new JSONObject();
//        if (userInfoService.findByUsername(username) != null) {
//            ret.put("existed", true);
//
//        } else {
//            ret.put("existed", false);
//        }
//        JsonHelper.jsonToResponse(response, ret);
//    }
//
//    /**
//     * @author:pis
//     * @description: 查看所有用户
//     * @date: 14:16 2018/4/13
//     */
//
//    @RequestMapping(value = "/checkUser", method = RequestMethod.GET)
//    public void checkUser(HttpServletResponse response) {
//        List<UserInfo> userInfos = userInfoService.findAllUser();
//        JSONObject ret = new JSONObject();
//        JSONArray userInfoArray = new JSONArray();
//        for (UserInfo userInfo : userInfos) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(globalUsername, userInfo.getUsername());
//            jsonObject.put(globalName, userInfo.getName());
//            List<SysRole> list = userInfo.getRoleList();
//            JSONArray list1 = new JSONArray();
//            for (SysRole sysRole : list) {
//                list1.put(sysRole.getSrid());
//            }
//            jsonObject.put("role", list1);
//            jsonObject.put("level",userInfo.getRealLevel());
//            jsonObject.put(globalPoints,userInfo.getPoints());
//            userInfoArray.put(jsonObject);
//        }
//        ret.put("users", userInfoArray);
//        JsonHelper.jsonToResponse(response, ret);
//
//    }
//
//
//    /**
//     * @author:pis
//     * @description: 删除用户
//     * @date: 14:16 2018/4/13
//     */
//
//    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
//    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, String[]> map = request.getParameterMap();
//        String username = map.get(globalUsername)[0];
//        JSONObject ret = new JSONObject();
//
//        ret.put(globalMes, userInfoService.deleteUser(username));
//        JsonHelper.jsonToResponse(response, ret);
//    }
//
//    /**
//     * @author:pis
//     * @description: 积分提现充值
//     * @date: 14:41 2018/4/13
//     */
//
//    @RequestMapping(value = "/changePoints", method = RequestMethod.POST)
//    public void changePoints(HttpServletRequest request, HttpServletResponse response) {
//        JSONObject jsonObject = JsonHelper.requestToJson(request);
//        String username = (String) jsonObject.get(globalUsername);
//        Integer points = (Integer) jsonObject.get(globalPoints);
//        UserInfo userInfo = userInfoService.findByUsername(username);
//        JSONObject ret = new JSONObject();
//        if (userInfo != null) {
//            userInfo.setPoints(userInfo.getPoints() + points);
//            ret.put(globalMes, userInfoService.modifyUser(userInfo));
//        } else
//            ret.put("ret", "null");
//        JsonHelper.jsonToResponse(response, ret);
//
//    }
//
//    /**
//     * @author:pis
//     * @description: 修改积分
//     * @date: 14:45 2018/4/13
//     */
//
//    @RequestMapping(value = "/managePoints", method = RequestMethod.POST)
//    public void managePoints(HttpServletRequest request, HttpServletResponse response) {
//        JSONObject jsonObject = JsonHelper.requestToJson(request);
//        String username = (String) jsonObject.get(globalUsername);
//        Integer points = (Integer) jsonObject.get(globalPoints);
//        UserInfo userInfo = userInfoService.findByUsername(username);
//        JSONObject ret = new JSONObject();
//        if (userInfo != null) {
//            userInfo.setPoints(points);
//            ret.put(globalMes, userInfoService.modifyUser(userInfo));
//        } else
//            ret.put("ret", "null");
//        JsonHelper.jsonToResponse(response, ret);
//    }
//
//    /**
//     * @author:pis
//     * @description: 奖励积分
//     * @date: 14:45 2018/4/13
//     */
//
//    @RequestMapping(value = "/Bonus", method = RequestMethod.POST)
//    public void bonus(HttpServletRequest request, HttpServletResponse response) {
//        JSONObject jsonObject = JsonHelper.requestToJson(request);
//        BonusHistory bonusHistory = new BonusHistory();
//        bonusHistory.setPoints((Integer) jsonObject.get(globalPoints));
//        bonusHistory.setTaskID((Integer) jsonObject.get("taskID"));
//        bonusHistory.setWorkerName((String) jsonObject.get("workerName"));
//        JSONObject ret = new JSONObject();
//        UserInfo userInfo = userInfoService.findByUsername(bonusHistory.getWorkerName());
//        if (userInfo != null) {
//            userInfo.setPoints(userInfo.getPoints() + bonusHistory.getPoints());
//            userInfo.setBonus(userInfo.getBonus() + bonusHistory.getPoints());
//            userInfo.setLevel(userInfo.getLevel() + bonusHistory.getPoints() / 100);
//
//            ret.put(globalMes, bonusHistoryService.addBonusHistory(bonusHistory) && userInfoService.modifyUser(userInfo));
//        } else
//            ret.put("ret", "null");
//        JsonHelper.jsonToResponse(response, ret);
//
//    }
}
