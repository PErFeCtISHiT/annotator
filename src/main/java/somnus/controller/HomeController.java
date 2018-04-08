package somnus.controller;

import com.google.gson.JsonArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import somnus.dao.SysRoleDao;
import somnus.dao.UserInfoDao;
import somnus.entity.SysRole;
import somnus.entity.UserInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class HomeController {
    @Resource
    private UserInfoDao userInfoDao ;
    @Resource
    private SysRoleDao sysRoleDao;
    @RequestMapping({"/","/index"})
    public String index(HttpServletResponse response){
        response.setContentType("application/json; charset=utf-8");

        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        System.out.println(userName.length());
        UserInfo userInfo = userInfoDao.findByUsername(userName);

        try (PrintWriter writer = response.getWriter()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",userInfo.getUsername());
            jsonObject.put("password",userInfo.getPassword());
            jsonObject.put("name",userInfo.getName());
            jsonObject.put("points",userInfo.getPoints());
            writer.append(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"/html/index.html";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Map<String, Object> map) throws Exception{
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception=" + exception);
        String msg = "";
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
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理
        return "/html/login.html";
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }


    /**
    *@author:pis
    *@description: signUp,point=0,id is not used in parse two
    *@date: 14:10 2018/4/8
    */
    @RequestMapping(value = "/signUp",method = RequestMethod.POST)
    @RequiresPermissions("signUp")
    public boolean signUp(HttpServletRequest request, HttpServletResponse response){
        StringBuilder jsonStr = new StringBuilder();
        try(InputStream inputStream = request.getInputStream()) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                jsonStr.append(inputStr);
            }
            streamReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(jsonStr.toString());
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(1);
        userInfo.setPoints(0);
        userInfo.setUsername((String) jsonObject.get("username"));
        ArrayList<SysRole> roleList = new ArrayList();
        JSONArray jsonArray = jsonObject.getJSONArray("roleList");
        for(Object obj : jsonArray){
            String srid = String.valueOf( obj);
            roleList.add(sysRoleDao.findBySysRoleId(srid));
        }
        userInfo.setRoleList(roleList);
        String password = new SimpleHash("MD5",jsonObject.get("password").toString(),jsonObject.get("username").toString(),2).toHex();
        userInfo.setPassword(password);
        userInfo.setSalt("123");
        userInfo.setName((String) jsonObject.get("username"));
        return userInfoDao.add(userInfo);
    }

}