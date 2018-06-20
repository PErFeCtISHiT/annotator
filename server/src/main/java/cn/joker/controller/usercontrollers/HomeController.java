package cn.joker.controller.usercontrollers;

import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.StdName;
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
     * @param userEntity 用户
     * @param response http
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
            msg.put(StdName.MES, "UnknownAccount");
            JsonHelper.jsonToResponse(response, msg);
            return;
        } catch (IncorrectCredentialsException e) {
            msg.put(StdName.MES, "IncorrectCredentials");
            JsonHelper.jsonToResponse(response, msg);
            return;
        }
        msg.put(StdName.MES, "success");
        JsonHelper.jsonToResponse(response, msg);
    }

    /**
     * @author:pis
     * @param response http
     * @description: 得到当前用户，无法通过junit单元测试，手动测
     * @date: 18:49 2018/4/13
     */
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    public void getCurrentUser(HttpServletResponse response) {
        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        UserEntity userInfo = userService.findByUsername(userName);
        JSONObject jsonObject = new JSONObject();
        if (userInfo != null) {
            jsonObject.put(StdName.USERNAME, userInfo.getUsername());
            jsonObject.put(StdName.LEVEL, userInfo.getLev());
            jsonObject.put(StdName.NICKNAME, userInfo.getNickname());
            jsonObject.put(StdName.POINTS, userInfo.getPoints());
            JSONArray list1 = new JSONArray();
            List<SysRoleEntity> list = userInfo.getRoleEntityList();
            for (SysRoleEntity sysRole : list) {
                list1.put(sysRole.getId());
                if(sysRole.getId().equals(3))
                    jsonObject.put(StdName.TYPE,StdName.REQUESTOR);
                else if (sysRole.getId().equals(4))
                    jsonObject.put(StdName.TYPE,StdName.WORKER);
            }
            jsonObject.put(StdName.ROLELIST, list1);
        } else {
            jsonObject.put(StdName.MES, StdName.NULL);
        }
        JsonHelper.jsonToResponse(response, jsonObject);
    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403";
    }

}