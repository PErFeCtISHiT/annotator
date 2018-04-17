package cn.joker.dao;

import cn.joker.entity.SysPermission;
import cn.joker.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:29 2018/3/28
 */
@Repository
public class SysPermissionDao {
    public SysPermission findBySysPermissionId(String spid) {

        JsonObject jsonObject = JsonHelper.openJson("json/permission.json");


        JsonArray jsonArray = jsonObject.getAsJsonArray("permissions");
        SysPermission sysPermission = new SysPermission();
        for (Object o : jsonArray) {

            JsonObject permissionJson = (JsonObject) o;

            if (permissionJson.get("spid").toString().equals(spid)) {

                sysPermission.setSpid(Integer.valueOf(permissionJson.get("spid").toString()));
                sysPermission.setPermission(JsonHelper.format(permissionJson.get("permission").toString()));
                return sysPermission;
            }
        }
        return null;
    }
}
