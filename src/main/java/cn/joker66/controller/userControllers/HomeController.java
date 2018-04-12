package cn.joker66.controller.userControllers;

import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/user")
public class HomeController {
    @Resource
    private UserInfoService userInfoService;
    @RequestMapping({"/","/index"})
    public void index(HttpServletResponse response){
        response.setContentType("application/json; charset=utf-8");

        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        UserInfo userInfo = userInfoService.findByUsername(userName);
        System.out.println(userName);

        try (PrintWriter writer = response.getWriter()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",userInfo.getUsername());
            jsonObject.put("level",userInfo.getLevel());
            jsonObject.put("name",userInfo.getName());
            jsonObject.put("points",userInfo.getPoints());
            writer.append(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception=" + exception);
        String msg ;
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> "+exception;
                System.out.println("else -- >" + exception);
            }
            response.setContentType("application/plain; charset=utf-8");
            try(PrintWriter writer = response.getWriter()){
                writer.append(msg);
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // 此方法不处理登录成功,由shiro进行处理
        return "/html/login.html";
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }

}