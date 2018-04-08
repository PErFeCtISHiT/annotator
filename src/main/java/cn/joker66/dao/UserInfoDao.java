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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDao {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username){

        UserInfo userInfo = new UserInfo();
        JsonObject jsonObject = Json.openJson("/user.json");
        assert jsonObject != null;
        JsonArray userArray = jsonObject.getAsJsonArray("users");

        SysRoleService sysRoleService = new SysRoleServiceImpl();
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
                    roleList.add(sysRoleService.findBySysRoleId(srid));
                }
                userInfo.setRoleList(roleList);
                userInfo.setPoints(Integer.valueOf(String.valueOf(object.get("points"))));
                userInfo.setUid(Integer.valueOf(String.valueOf(object.get("uid"))));
                userInfo.setUsername(username);
                userInfo.setState(Integer.valueOf(String.valueOf(object.get("state"))));
                return userInfo;
            }
        }

        return null;
    }

    public boolean add(UserInfo userInfo, List<Integer> list) {
        JsonObject json = Json.openJson("/user.json");
        assert json != null;
        JsonArray userArray = json.getAsJsonArray("users");
        StringBuilder newJson = new StringBuilder("{\"users\":[");
        for(Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (Json.format(object.get("username").toString()).equals(userInfo.getUsername())) {
                return false;
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
        JSONArray roleList = new JSONArray();
        for(Integer i : list){
            roleList.put(i);
        }
        jsonObject.put("roleList",roleList);
        newJson.append(jsonObject.toString());
        newJson.append("]}");
        String path = System.getProperty("user.dir") + "/src/main/resources/static/json/user.json";
        File file = new File(path);
        try (FileWriter fileWriter = new FileWriter(file,false)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(newJson.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}