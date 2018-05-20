package cn.joker.controller.usercontrollers;

import cn.joker.entity.UserEntity;
import cn.joker.sevice.UserService;
import cn.joker.util.JsonHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
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
    public UserEntity getCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.findByUsername(username);
    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403";
    }

}