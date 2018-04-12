package cn.joker66.dao;

import cn.joker66.entity.SysRole;
import cn.joker66.entity.UserInfo;
import cn.joker66.serviceImpl.SysRoleServiceImpl;
import cn.joker66.sevice.SysRoleService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import cn.joker66.util.Json;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDao {
    @Resource
    private SysRoleService sysRoleService = new SysRoleServiceImpl();
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username){

        UserInfo userInfo = new UserInfo();
        JsonObject jsonObject = Json.openJson("/user.json");
        assert jsonObject != null;
        JsonArray userArray = jsonObject.getAsJsonArray("users");

        for(Object o : userArray){
            JsonObject object = (JsonObject) o;
            if(Json.format(object.get("username").toString()).equals(username)){
                userInfo.setPassword(Json.format(object.get("password").toString()));
                userInfo.setSalt(Json.format(object.get("salt").toString()));
                userInfo.setName(Json.format(object.get("name").toString()));
                ArrayList<SysRole> roleList = new ArrayList();
                JsonArray jsonArray = object.getAsJsonArray("roleList");
                for(Object obj : jsonArray){
                    String srid = String.valueOf( obj);
                    System.out.println(sysRoleService);
                    roleList.add(sysRoleService.findBySysRoleId(srid));
                }
                userInfo.setRoleList(roleList);
                userInfo.setPoints(Integer.valueOf(String.valueOf(object.get("points"))));
                userInfo.setUid(Integer.valueOf(String.valueOf(object.get("uid"))));
                userInfo.setUsername(username);
                userInfo.setLevel(Integer.valueOf(String.valueOf(object.get("level"))));
                userInfo.setState(Integer.valueOf(String.valueOf(object.get("state"))));

                return userInfo;
            }
        }

        return null;
    }

    public String add(UserInfo userInfo) {
        JsonObject json = Json.openJson("/user.json");
        assert json != null;
        JsonArray userArray = json.getAsJsonArray("users");
        StringBuilder newJson = new StringBuilder("{\"users\":[");
        for(Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (Json.format(object.get("username").toString()).equals(userInfo.getUsername())) {
                return "exists";
            }
            newJson.append(object.toString());
            newJson.append(",");

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid",userInfo.getUid());
        jsonObject.put("username",userInfo.getUsername());
        jsonObject.put("password",userInfo.getPassword());
        jsonObject.put("name",userInfo.getName());
        jsonObject.put("salt",userInfo.getSalt());
        jsonObject.put("points",userInfo.getPoints());
        jsonObject.put("state",userInfo.getState());
        jsonObject.put("level",userInfo.getLevel());
        JSONArray roleList = new JSONArray();
        List<SysRole> list = userInfo.getRoleList();
        for(SysRole i : list){
            roleList.put(i.getSrid());
        }
        jsonObject.put("roleList",roleList);
        newJson.append(jsonObject.toString());
        newJson.append("]}");
        return this.updateJson(newJson);
    }

    public String modifyUser(UserInfo userInfo) {
        JsonObject json = Json.openJson("/user.json");
        assert json != null;
        JsonArray userArray = json.getAsJsonArray("users");
        StringBuilder newJson = new StringBuilder("{\"users\":[");
        for(Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (Json.format(object.get("username").toString()).equals(userInfo.getUsername())) {
                JSONObject newUser = new JSONObject();
                newUser.put("username",userInfo.getUsername());
                newUser.put("password",userInfo.getPassword());
                newUser.put("salt",userInfo.getSalt());
                newUser.put("uid",Integer.valueOf(String.valueOf(object.get("uid"))));
                newUser.put("name",userInfo.getName());
                JSONArray roleList = new JSONArray();
                List<SysRole> list = userInfo.getRoleList();
                for(SysRole i : list){
                    roleList.put(i.getSrid());
                }
                newUser.put("roleList",roleList);
                newUser.put("points",Integer.valueOf(String.valueOf(object.get("points"))));
                newUser.put("level",Integer.valueOf(String.valueOf(object.get("level"))));
                newUser.put("state",Integer.valueOf(String.valueOf(object.get("state"))));
            }
            newJson.append(object.toString());
            newJson.append(",");

        }
        newJson.deleteCharAt(newJson.lastIndexOf(","));
        newJson.append("]}");
        return this.updateJson(newJson);

    }
    private String updateJson(StringBuilder newJson){
        String path = System.getProperty("user.dir") + "/src/main/resources/static/json/user.json";
        File file = new File(path);
        try (FileWriter fileWriter = new FileWriter(file,false)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(newJson.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }
        return "success";
    }
}