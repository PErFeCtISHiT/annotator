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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UserInfoDao {
    private SysRoleDao sysRoleDao = new SysRoleDao();

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
                    roleList.add(sysRoleDao.findBySysRoleId(srid));
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

    public boolean addUser(UserInfo userInfo) {
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

    public boolean modifyUser(UserInfo userInfo) {
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
                newJson.append(newUser.toString());
                newJson.append(",");
            }
            else {
                newJson.append(object.toString());
                newJson.append(",");
            }

        }
        newJson.deleteCharAt(newJson.lastIndexOf(","));
        newJson.append("]}");
        return this.updateJson(newJson);

    }
    /**
    *@author:pis
    *@description: 更新uers的json文件
    *@date: 15:04 2018/4/13
    */
    private boolean updateJson(StringBuilder newJson){ return Json.modifyJson(newJson,"user.json");
    }

    public List<UserInfo> findAllUser() {

        List<UserInfo> ret = new ArrayList<>();
        JsonObject jsonObject = Json.openJson("/user.json");
        assert jsonObject != null;
        JsonArray userArray = jsonObject.getAsJsonArray("users");
        for(Object o : userArray) {
            JsonObject object = (JsonObject) o;
            UserInfo userInfo = new UserInfo();
            userInfo.setPassword(Json.format(object.get("password").toString()));
            userInfo.setSalt(Json.format(object.get("salt").toString()));
            userInfo.setName(Json.format(object.get("name").toString()));
            ArrayList<SysRole> roleList = new ArrayList();
            JsonArray jsonArray = object.getAsJsonArray("roleList");
            for(Object obj : jsonArray){
                String srid = String.valueOf( obj);
                roleList.add(sysRoleDao.findBySysRoleId(srid));
            }
            userInfo.setRoleList(roleList);
            userInfo.setPoints(Integer.valueOf(String.valueOf(object.get("points"))));
            userInfo.setUid(Integer.valueOf(String.valueOf(object.get("uid"))));
            userInfo.setUsername(Json.format(object.get("username").toString()));
            userInfo.setLevel(Integer.valueOf(String.valueOf(object.get("level"))));
            userInfo.setState(Integer.valueOf(String.valueOf(object.get("state"))));
            ret.add(userInfo);
        }
        return ret;
    }

    public boolean deleteUser(String username) {
        JsonObject json = Json.openJson("/user.json");
        assert json != null;
        JsonArray userArray = json.getAsJsonArray("users");
        StringBuilder newJson = new StringBuilder("{\"users\":[");
        for(Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if(!Json.format(object.get("username").toString()).equals(username)) {
                newJson.append(object.toString());
                newJson.append(",");
            }

        }
        newJson.deleteCharAt(newJson.lastIndexOf(","));
        newJson.append("]}");
        return this.updateJson(newJson);
    }
}