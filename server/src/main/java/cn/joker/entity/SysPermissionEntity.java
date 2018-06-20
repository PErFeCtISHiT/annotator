package cn.joker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:55 2018/5/4
 */
@Entity
@Table(name = "sys_Permission", schema = "imgannotator", catalog = "")
public class SysPermissionEntity implements Serializable {
    private Integer id;
    private String permission;
    private List<SysRoleEntity> sysRoleEntityList;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "permission", nullable = false, length = 200)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @ManyToMany(mappedBy = "sysPermissionEntityList", cascade = CascadeType.MERGE)
    @JsonIgnore
    public List<SysRoleEntity> getSysRoleEntityList() {
        return sysRoleEntityList;
    }

    public void setSysRoleEntityList(List<SysRoleEntity> sysRoleEntityList) {
        this.sysRoleEntityList = sysRoleEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SysPermissionEntity that = (SysPermissionEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(permission, that.permission) &&
                Objects.equals(sysRoleEntityList, that.sysRoleEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, permission, sysRoleEntityList);
    }
}
