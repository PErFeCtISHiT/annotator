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
@Table(name = "sys_role", schema = "imgannotator", catalog = "")
public class SysRoleEntity implements Serializable {
    private Integer id;
    private String role;
    private String description;
    private List<UserEntity> userEntityList;
    private List<SysPermissionEntity> sysPermissionEntityList;

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
    @Column(name = "role", nullable = false, length = 200)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    @ManyToMany(mappedBy = "roleEntityList", cascade = CascadeType.MERGE)
    @JsonIgnore
    public List<UserEntity> getUserEntityList() {
        return userEntityList;
    }

    public void setUserEntityList(List<UserEntity> userEntityList) {
        this.userEntityList = userEntityList;
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(referencedColumnName = "ID")}
            , inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})
    @JsonIgnore
    public List<SysPermissionEntity> getSysPermissionEntityList() {
        return sysPermissionEntityList;
    }

    public void setSysPermissionEntityList(List<SysPermissionEntity> sysPermissionEntityList) {
        this.sysPermissionEntityList = sysPermissionEntityList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SysRoleEntity that = (SysRoleEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(role, that.role) &&
                Objects.equals(description, that.description) &&
                Objects.equals(userEntityList, that.userEntityList) &&
                Objects.equals(sysPermissionEntityList, that.sysPermissionEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, role, description, userEntityList, sysPermissionEntityList);
    }
}
