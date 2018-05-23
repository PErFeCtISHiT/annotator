package cn.joker.controller.usercontrollers;

import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.UserService;
import cn.joker.util.JsonHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class HomeController {
    @Resource
    private UserService userService;


    /**
     * @author:pis
     * @description: 登陆
     * @date: 13:35 2018/4/13
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody UserEntity userEntity, HttpServletResponse response) {

        JSONObject msg = new JSONObject();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userEntity.getUsername(), userEntity.getPasswr());
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            msg.put(stdName.MES, "UnknownAccount");
            JsonHelper.jsonToResponse(response, msg);
            return;
        } catch (IncorrectCredentialsException e) {
            msg.put(stdName.MES, "IncorrectCredentials");
            JsonHelper.jsonToResponse(response, msg);
            return;
        }
        msg.put(stdName.MES, "success");
        JsonHelper.jsonToResponse(response, msg);
    }

    /**
     * @author:pis
     * @description: 得到当前用户，无法通过junit单元测试，手动测
     * @date: 18:49 2018/4/13
     */
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    public void getCurrentUser(HttpServletResponse response) {
        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        UserEntity userInfo = userService.findByUsername(userName);
        JSONObject jsonObject = new JSONObject();
        if (userInfo != null) {
            jsonObject.put(stdName.USERNAME, userInfo.getUsername());
            jsonObject.put(stdName.LEVEL, userInfo.getLev());
            jsonObject.put(stdName.NICKNAME, userInfo.getNickname());
            jsonObject.put(stdName.POINTS, userInfo.getPoints());
            JSONArray list1 = new JSONArray();
            List<SysRoleEntity> list = userInfo.getRoleEntityList();
            for (SysRoleEntity sysRole : list) {
                list1.put(sysRole.getId());
            }
            jsonObject.put(stdName.ROLELIST, list1);
        } else {
            jsonObject.put(stdName.MES, stdName.NULL);
        }
        JsonHelper.jsonToResponse(response, jsonObject);
    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403";
    }

}