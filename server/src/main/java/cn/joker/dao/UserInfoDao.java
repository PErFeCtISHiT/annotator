package cn.joker.dao;

import cn.joker.entity.SysRole;
import cn.joker.entity.UserInfo;
import cn.joker.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserInfoDao {
    private String globalUserJson = "json/user.json";
    private String globalUsers = "users";
    private String globalUsername = "username";
    private String globalPasswr = "password";
    private String globalRoleList = "roleList";
    private String globalPoints = "points";
    private String globalLevel = "level";
    private String globalState = "state";
    private String globalSalt = "salt";
    private String globalName = "name";
    private String globalUid = "uid";
    private String globalJsonUsers = "{\"users\":[";
    private String globalBonus = "bonus";
    @Resource
    private SysRoleDao sysRoleDao;

    public UserInfo findByUsername(String username) {

        JsonObject jsonObject = JsonHelper.openJson(globalUserJson);
        assert jsonObject != null;
        JsonArray userArray = jsonObject.getAsJsonArray(globalUsers);

        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (JsonHelper.format(object.get(globalUsername).toString()).equals(username)) {

                return this.warpUserInfo(object);
            }
        }

        return null;
    }

    public boolean addUser(UserInfo userInfo) {
        JsonObject json = JsonHelper.openJson(globalUserJson);
        assert json != null;
        JsonArray userArray = json.getAsJsonArray(globalUsers);
        StringBuilder newJson = new StringBuilder(globalJsonUsers);
        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (JsonHelper.format(object.get(globalUsername).toString()).equals(userInfo.getUsername())) {
                return false;
            }
            newJson.append(object.toString());
            newJson.append(",");

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(globalUid, userInfo.getUid());
        jsonObject.put(globalUsername, userInfo.getUsername());
        jsonObject.put(globalPasswr, userInfo.getPassword());
        jsonObject.put(globalName, userInfo.getName());
        jsonObject.put(globalSalt, userInfo.getSalt());
        jsonObject.put(globalPoints, userInfo.getPoints());
        jsonObject.put(globalState, userInfo.getState());
        jsonObject.put(globalLevel, userInfo.getLevel());
        jsonObject.put(globalBonus, userInfo.getBonus());
        JSONArray roleList = new JSONArray();
        List<SysRole> list = userInfo.getRoleList();
        for (SysRole i : list) {
            roleList.put(i.getSrid());
        }
        jsonObject.put(globalRoleList, roleList);
        newJson.append(jsonObject.toString());
        newJson.append("]}");
        return this.updateJson(newJson);
    }

    public boolean modifyUser(UserInfo userInfo) {
        JsonObject json = JsonHelper.openJson(globalUserJson);
        assert json != null;
        JsonArray userArray = json.getAsJsonArray(globalUsers);
        StringBuilder newJson = new StringBuilder(globalJsonUsers);
        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (JsonHelper.format(object.get(globalUsername).toString()).equals(userInfo.getUsername())) {
                JSONObject newUser = new JSONObject();
                newUser.put(globalUsername, userInfo.getUsername());
                newUser.put(globalPasswr, userInfo.getPassword());
                newUser.put(globalSalt, userInfo.getSalt());
                newUser.put(globalUid, userInfo.getUid());
                newUser.put(globalName, userInfo.getName());
                JSONArray roleList = new JSONArray();
                List<SysRole> list = userInfo.getRoleList();
                for (SysRole i : list) {
                    roleList.put(i.getSrid());
                }
                newUser.put(globalRoleList, roleList);
                newUser.put(globalPoints, userInfo.getPoints());
                newUser.put(globalLevel, userInfo.getLevel());
                newUser.put(globalState, userInfo.getState());
                newUser.put(globalBonus, userInfo.getBonus());
                newJson.append(newUser.toString());
                newJson.append(",");
            } else {
                newJson.append(object.toString());
                newJson.append(",");
            }

        }
        if(newJson.lastIndexOf(",") != -1){
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");
        return this.updateJson(newJson);

    }

    /**
     * @author:pis
     * @description: 更新uers的json文件
     * @date: 15:04 2018/4/13
     */
    private boolean updateJson(StringBuilder newJson) {
        return JsonHelper.modifyJson(newJson, globalUserJson);
    }

    public List<UserInfo> findAllUser() {

        List<UserInfo> ret = new ArrayList<>();
        JsonObject jsonObject = JsonHelper.openJson(globalUserJson);
        assert jsonObject != null;
        JsonArray userArray = jsonObject.getAsJsonArray(globalUsers);
        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            ret.add(this.warpUserInfo(object));
        }
        return ret;
    }

    public boolean deleteUser(String username) {
        JsonObject json = JsonHelper.openJson(globalUserJson);
        assert json != null;
        JsonArray userArray = json.getAsJsonArray(globalUsers);
        StringBuilder newJson = new StringBuilder(globalJsonUsers);
        for (Object o : userArray) {
            JsonObject object = (JsonObject) o;
            if (!JsonHelper.format(object.get(globalUsername).toString()).equals(username)) {
                newJson.append(object.toString());
                newJson.append(",");
            }

        }
        if(newJson.lastIndexOf(",") != -1){
            newJson.deleteCharAt(newJson.lastIndexOf(","));
        }
        newJson.append("]}");
        return this.updateJson(newJson);
    }

    private UserInfo warpUserInfo(JsonObject object) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(JsonHelper.format(object.get(globalPasswr).toString()));
        userInfo.setSalt(JsonHelper.format(object.get(globalSalt).toString()));
        userInfo.setName(JsonHelper.format(object.get(globalName).toString()));
        ArrayList<SysRole> roleList = new ArrayList();
        JsonArray jsonArray = object.getAsJsonArray(globalRoleList);
        for (Object obj : jsonArray) {
            String srid = String.valueOf(obj);
            roleList.add(sysRoleDao.findBySysRoleId(srid));
        }
        userInfo.setRoleList(roleList);
        userInfo.setPoints(Integer.valueOf(String.valueOf(object.get(globalPoints))));
        userInfo.setUid(Integer.valueOf(String.valueOf(object.get(globalUid))));
        userInfo.setUsername(JsonHelper.format(object.get(globalUsername).toString()));
        userInfo.setLevel(Double.valueOf(String.valueOf(object.get(globalLevel))));
        userInfo.setState(Integer.valueOf(String.valueOf(object.get(globalState))));
        userInfo.setBonus(Integer.valueOf(String.valueOf(object.get(globalBonus))));
        return userInfo;
    }
}