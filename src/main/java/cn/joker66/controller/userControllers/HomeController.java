package cn.joker66.controller.userControllers;

import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.UserInfoService;
import cn.joker66.util.Json;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        JSONObject jsonObject = Json.requestToJson(request);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(jsonObject.get("username").toString(), jsonObject.get("password").toString());
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            msg.put("mes", "UnknownAccount");
            Json.JsonToResponse(response, msg);
            return;
        } catch (IncorrectCredentialsException e) {
            msg.put("mes", "IncorrectCredentials");
            Json.JsonToResponse(response, msg);
            return;
        }
//        String exception = (String) request.getAttribute("shiroLoginFailure");
//        System.out.println("exception=" + exception);
//        if (exception != null) {
//            if (UnknownAccountException.class.getName().equals(exception)) {
//                System.out.println("UnknownAccountException -- > 账号不存在：");
//                msg.put("mes","UnknownAccountException -- > 账号不存在：");
//            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
//                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
//                msg.put("mes","IncorrectCredentialsException -- > 密码不正确：");
//            } else if ("kaptchaValidateFailed".equals(exception)) {
//                System.out.println("kaptchaValidateFailed -- > 验证码错误");
//                msg.put("mes","kaptchaValidateFailed -- > 验证码错误");
//            } else {
//                msg.put("mes","else >> "+exception);
//                System.out.println("else -- >" + exception);
//            }
//        }
//        else{
//            msg.put("mes","success");
//            System.out.println("success");
//        }
        msg.put("mes", "success");
        Json.JsonToResponse(response, msg);
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
        jsonObject.put("username", userInfo.getUsername());
        jsonObject.put("level", userInfo.getLevel());
        jsonObject.put("name", userInfo.getName());
        jsonObject.put("points", userInfo.getPoints());

        Json.JsonToResponse(response, jsonObject);

    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        System.out.println("------没有权限-------");
        return "403";
    }

}