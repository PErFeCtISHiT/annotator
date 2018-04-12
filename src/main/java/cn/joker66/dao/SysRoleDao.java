package cn.joker66.dao;

import cn.joker66.entity.SysPermission;
import cn.joker66.entity.SysRole;
import cn.joker66.serviceImpl.SysPermissionServiceImpl;
import cn.joker66.sevice.SysPermissionService;
import cn.joker66.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:29 2018/3/28
 */
public class SysRoleDao {
    @Resource
    private SysPermissionService sysPermissionService;
    public SysRole findBySysRoleId(String srid){
        JsonObject jsonObject = Json.openJson("/role.json");


        JsonArray jsonArray = jsonObject.getAsJsonArray("roles");
        SysRole sysRole = new SysRole();

        for(Object o : jsonArray){
            JsonObject roleJson = (JsonObject) o;
            if(roleJson.get("srid").toString().equals(srid)){

                sysRole.setSrid(Integer.valueOf(srid));

                sysRole.setDescription(String.valueOf(Json.format(roleJson.get("description").toString())));

                sysRole.setRole(String.valueOf(Json.format(roleJson.get("role").toString())));
                ArrayList<SysPermission> sysPermissionArrayList = new ArrayList<>();
                JsonArray Array = roleJson.getAsJsonArray("permissionList");
                for(Object obj : Array){
                    String spid = String.valueOf( obj);
                    sysPermissionArrayList.add(sysPermissionService.findBySysPermissionId(spid));
                }
                sysRole.setPermissions(sysPermissionArrayList);
                return sysRole;
            }
        }
        return null;
    }
}
