package cn.joker66.dao;

import cn.joker66.entity.SysPermission;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cn.joker66.util.Json;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:29 2018/3/28
 */
@Repository
public class SysPermissionDao {
    public SysPermission findBySysPermissionId(String spid){

        JsonObject jsonObject = Json.openJson("/permission.json");


        JsonArray jsonArray = jsonObject.getAsJsonArray("permissions");
        SysPermission sysPermission = new SysPermission();
        for(Object o : jsonArray) {
            JsonObject permissionJson = (JsonObject) o;

            if (permissionJson.get("spid").toString().equals(spid)) {

                sysPermission.setSpid(Integer.valueOf(permissionJson.get("spid").toString()));
                sysPermission.setPermission(Json.format(permissionJson.get("permission").toString()));
                return sysPermission;
            }
        }
        return null;
    }
}
