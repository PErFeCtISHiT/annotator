package somnus.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import somnus.entity.SysRole;
import somnus.entity.UserInfo;
import somnus.serviceImpl.SysRoleServiceImpl;
import somnus.sevice.SysRoleService;
import somnus.util.Json;

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

    public boolean add(UserInfo userInfo) {
        JsonObject json = Json.openJson("/user.json");
        assert json != null;
        JsonArray userArray = json.getAsJsonArray("users");
        JSONObject newObj = new JSONObject();
        JSONArray newArray = new JSONArray();
        for(Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (Json.format(object.get("username").toString()).equals(userInfo.getUsername())) {
                return false;
            }
            newArray.put(o);

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid",userInfo.getUid());
        jsonObject.put("username",userInfo.getUsername());
        jsonObject.put("password",userInfo.getPassword());
        jsonObject.put("name",userInfo.getName());
        jsonObject.put("salt",userInfo.getSalt());
        jsonObject.put("points",userInfo.getPoints());
        jsonObject.put("stats",userInfo.getState());
        List<SysRole> list = userInfo.getRoleList();
        JSONArray roleList = new JSONArray();
        for(SysRole sysRole : list){
            roleList.put(sysRole.getSrid());
        }
        jsonObject.put("roleList",roleList);
        newArray.put(jsonObject);
        newObj.put("users",newArray);
        String path = System.getProperty("user.dir") + "/src/main/resources/static/json/user.json";
        File file = new File(path);
        try (FileWriter fileWriter = new FileWriter(file,false)){
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(newObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}