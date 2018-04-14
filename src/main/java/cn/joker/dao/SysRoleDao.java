package cn.joker.dao;

import cn.joker.entity.SysPermission;
import cn.joker.entity.SysRole;
import cn.joker.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:29 2018/3/28
 */
@Repository
public class SysRoleDao {
    private SysPermissionDao sysPermissionDao = new SysPermissionDao();

    public SysRole findBySysRoleId(String srid) {
        JsonObject jsonObject = Json.openJson("json/role.json");


        JsonArray jsonArray = jsonObject.getAsJsonArray("roles");
        SysRole sysRole = new SysRole();

        for (Object o : jsonArray) {
            JsonObject roleJson = (JsonObject) o;
            if (roleJson.get("srid").toString().equals(srid)) {

                sysRole.setSrid(Integer.valueOf(srid));

                sysRole.setDescription(String.valueOf(Json.format(roleJson.get("description").toString())));

                sysRole.setRole(String.valueOf(Json.format(roleJson.get("role").toString())));
                ArrayList<SysPermission> sysPermissionArrayList = new ArrayList<>();
                JsonArray array = roleJson.getAsJsonArray("permissionList");
                for (Object obj : array) {
                    String spid = String.valueOf(obj);
                    sysPermissionArrayList.add(sysPermissionDao.findBySysPermissionId(spid));
                }
                sysRole.setPermissions(sysPermissionArrayList);
                return sysRole;
            }
        }
        return null;
    }
}
