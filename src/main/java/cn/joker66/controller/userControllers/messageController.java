package cn.joker66.controller.userControllers;

import cn.joker66.entity.SysRole;
import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.SysRoleService;
import cn.joker66.sevice.UserInfoService;
import cn.joker66.util.Json;
import cn.joker66.util.PasswordHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 19:51 2018/4/11
 */
@RequestMapping("/user")
public class messageController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     *@author:pis
     *@description: signUp,point=0,id is not used in parse two
     *@date: 14:10 2018/4/8
     */
    @RequestMapping(value = "/signUp",method = RequestMethod.POST)
    @RequiresPermissions("signUp")
    public void signUp(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = Json.requestToJson(request);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(1);
        userInfo.setPoints(0);
        userInfo.setUsername((String) jsonObject.get("username"));

        JSONArray jsonArray = jsonObject.getJSONArray("roleList");
        List<SysRole> roleList = new ArrayList<>();
        for(Object obj : jsonArray){
            String srid = String.valueOf( obj);
            roleList.add(sysRoleService.findBySysRoleId(srid));
        }
        userInfo.setRoleList(roleList);
        userInfo.setPassword((String) jsonObject.get("password"));
        PasswordHelper.encryptPassword(userInfo);
        userInfo.setLevel(1);
        userInfo.setName((String) jsonObject.get("username"));
        userInfo.setState(1);
        JSONObject ret = new JSONObject();

        response.setContentType("application/json; charset=utf-8");
        ret.put("mes",userInfoService.addUser(userInfo));
        try(PrintWriter writer = response.getWriter()){
            writer.write(ret.toString());
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    @RequiresPermissions("submitAssignment")
    public void modifyMessage(HttpServletRequest request, HttpServletResponse response){
        JSONObject newUser = Json.requestToJson(request);
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUsername((String) newUser.get("username"));
        newUserInfo.setPassword((String) newUser.get("password"));
        newUserInfo.setName((String) newUser.get("name"));
        JSONArray jsonArray = newUser.getJSONArray("roleList");
        List<SysRole> roleList = new ArrayList<>();
        for(Object obj : jsonArray){
            String srid = String.valueOf( obj);
            roleList.add(sysRoleService.findBySysRoleId(srid));
        }
        newUserInfo.setRoleList(roleList);
        PasswordHelper.encryptPassword(newUserInfo);

        JSONObject ret = new JSONObject();

        response.setContentType("application/json; charset=utf-8");
        ret.put("mes",userInfoService.modifyUser(newUserInfo));
        try(PrintWriter writer = response.getWriter()){
            writer.write(ret.toString());
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
