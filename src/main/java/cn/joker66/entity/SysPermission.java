package cn.joker66.entity;

import java.io.Serializable;


public class SysPermission implements Serializable {

    private Integer spid;//主键.
    private String permission;//权限

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }


}