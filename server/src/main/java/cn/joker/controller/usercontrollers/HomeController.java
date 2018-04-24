package cn.joker.controller.usercontrollers;

import cn.joker.entity.SysRole;
import cn.joker.entity.UserInfo;
import cn.joker.sevice.UserInfoService;
import cn.joker.util.JsonHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class HomeController {
    @Resource
    private UserInfoService userInfoService;


    /**
     * @author:pis
     * @description: 登陆
     * @date: 13:35 2018/4/13
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) {

        JSONObject msg = new JSONObject();
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(jsonObject.get("username").toString(), jsonObject.get("password").toString());
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            msg.put("mes", "UnknownAccount");
            JsonHelper.jsonToResponse(response, msg);
            return;
        } catch (IncorrectCredentialsException e) {
            msg.put("mes", "IncorrectCredentials");
            JsonHelper.jsonToResponse(response, msg);
            return;
        }
        msg.put("mes", "success");
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
        UserInfo userInfo = userInfoService.findByUsername(userName);
        JSONObject jsonObject = new JSONObject();
        if(userInfo != null) {
            jsonObject.put("username", userInfo.getUsername());
            jsonObject.put("level", userInfo.getLevel());
            jsonObject.put("name", userInfo.getName());
            jsonObject.put("points", userInfo.getPoints());
            JSONArray list1 = new JSONArray();
            List<SysRole> list = userInfo.getRoleList();
            for (SysRole sysRole : list) {
                list1.put(sysRole.getSrid());
            }
            jsonObject.put("role", list1);
        }
        else{
            jsonObject.put("ret","null");
        }
        JsonHelper.jsonToResponse(response, jsonObject);

    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403";
    }

}