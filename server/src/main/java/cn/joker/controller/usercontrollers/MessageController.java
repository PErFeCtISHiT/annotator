package cn.joker.controller.usercontrollers;

import cn.joker.dao.UserRepository;
import cn.joker.entity.BonusHistoryEntity;
import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.*;
import cn.joker.util.JsonHelper;
import cn.joker.util.PasswordHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepository userRepository;


    /**
     * @author:pis
     * @description: 注册，point，uid，state初始化为0
     * @date: 14:10 2018/4/8
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void signUp(@RequestBody UserEntity userEntity, HttpServletResponse response) {
        List<SysRoleEntity> sysRoleEntities = new ArrayList<>();
        sysRoleEntities.add((SysRoleEntity) sysRoleService.findByID(3));
        sysRoleEntities.add((SysRoleEntity) sysRoleService.findByID(4));
        userEntity.setRoleEntityList(sysRoleEntities);
        userEntity.setPoints(100);
        PasswordHelper.encryptPassword(userEntity);
        userEntity.setLev(1);
        userEntity.setState(1);
        userEntity.setBonus(0);
        JSONObject ret = new JSONObject();
        userRepository.saveAndFlush(userEntity);
        ret.put(stdName.MES, userService.add(userEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 修改一个用户的信息
     * @date: 13:36 2018/4/13
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public void modifyMessage(@RequestBody UserEntity userEntity, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        UserEntity newEntity = userService.findByUsername(userEntity.getUsername());
        newEntity.setNickname(userEntity.getNickname());
        newEntity.setPasswr(userEntity.getPasswr());
        newEntity.setEmail(userEntity.getEmail());
        PasswordHelper.encryptPassword(newEntity);
        ret.put(stdName.MES, userService.modify(newEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 按用户名查找一个用户是否存在
     * @date: 13:36 2018/4/13
     */
    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public void findUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(stdName.USERNAME)[0];

        JSONObject ret = new JSONObject();
        if (userService.findByUsername(username) != null) {
            ret.put(stdName.EXISTED, true);

        } else {
            ret.put(stdName.EXISTED, false);
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
     * @author:pis
     * @description: 删除用户
     * @date: 14:16 2018/4/13
     */

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        String username = map.get(stdName.USERNAME)[0];
        JSONObject ret = new JSONObject();
        UserEntity userEntity = userService.findByUsername(username);
        ret.put(stdName.MES, userService.delete(userEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 积分提现充值
     * @date: 14:41 2018/4/13
     */

    @RequestMapping(value = "/changePoints", method = RequestMethod.POST)
    public void changePoints(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(stdName.USERNAME);
        Integer points = jsonObject.getInt(stdName.POINTS);
        UserEntity userEntity = userService.findByUsername(username);
        JSONObject ret = new JSONObject();
        if (userEntity != null) {
            userEntity.setPoints(userEntity.getPoints() + points);
            ret.put(stdName.MES, userService.modify(userEntity));
        } else
            ret.put(stdName.MES, stdName.NULL);
        JsonHelper.jsonToResponse(response, ret);

    }

    /**
     * @author:pis
     * @description: 修改积分
     * @date: 14:45 2018/4/13
     */

    @RequestMapping(value = "/managePoints", method = RequestMethod.POST)
    public void managePoints(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(stdName.USERNAME);
        Integer points = jsonObject.getInt(stdName.POINTS);
        UserEntity userEntity = userService.findByUsername(username);
        JSONObject ret = new JSONObject();
        if (userEntity != null) {
            userEntity.setPoints(points);
            ret.put(stdName.MES, userService.modify(userEntity));
        } else
            ret.put(stdName.MES, stdName.NULL);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 奖励积分
     * @date: 14:45 2018/4/13
     */

    @RequestMapping(value = "/Bonus", method = RequestMethod.POST)
    public void bonus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        BonusHistoryEntity bonusHistory = new BonusHistoryEntity();
        bonusHistory.setPoints(jsonObject.getInt(stdName.POINTS));
        bonusHistory.setBonusHistory_task((TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID)));
        UserEntity userEntity = userService.findByUsername(jsonObject.getString(stdName.WORKERNAME));
        JSONObject ret = new JSONObject();
        if (userEntity != null) {
            bonusHistory.setBonusHistory_user(userEntity);
            userEntity.setPoints(userEntity.getPoints() + bonusHistory.getPoints());
            userEntity.setBonus(userEntity.getBonus() + bonusHistory.getPoints());

            ret.put(stdName.MES, bonusHistoryService.add(bonusHistory) && userService.modify(userEntity));
        } else
            ret.put(stdName.MES, stdName.NULL);
        JsonHelper.jsonToResponse(response, ret);

    }
}
