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
@Table(name = "user",schema = "imgannotator", catalog = "")
public class UserEntity implements Serializable {
    private Integer id;
    private String username;
    private String passwr;
    private String salt;
    private Integer bonus;
    private String nickname;
    private Integer lev;
    private Integer points;
    private Integer state;
    private List<SysRoleEntity> roleEntityList;
    private List<AbilityEntity> abilityEntityList;
    private List<WorkerMatrixEntity> workerMatrixEntities;
    private Integer type1Num;
    private Integer type2Num;
    private Integer type3Num;

    @Basic
    @Column(name = "num1", nullable = true)
    public Integer getType1Num() {
        return type1Num;
    }

    public void setType1Num(Integer type1Num) {
        this.type1Num = type1Num;
    }
    @Basic
    @Column(name = "num2", nullable = true)
    public Integer getType2Num() {
        return type2Num;
    }

    public void setType2Num(Integer type2Num) {
        this.type2Num = type2Num;
    }
    @Basic
    @Column(name = "num3", nullable = true)
    public Integer getType3Num() {
        return type3Num;
    }

    public void setType3Num(Integer type3Num) {
        this.type3Num = type3Num;
    }

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
    @Column(name = "username", nullable = false, length = 200)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "passwr", nullable = false, length = 200)
    public String getPasswr() {
        return passwr;
    }

    public void setPasswr(String passwr) {
        this.passwr = passwr;
    }

    @Basic
    @Column(name = "salt", nullable = false, length = 200)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Basic
    @Column(name = "bonus", nullable = true)
    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    @Basic
    @Column(name = "nickname", nullable = true, length = 200)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "lev", nullable = true)
    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    @Basic
    @Column(name = "points", nullable = true)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Basic
    @Column(name = "state", nullable = true)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }



    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(referencedColumnName = "ID")}
            , inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})
    @JsonIgnore
    public List<SysRoleEntity> getRoleEntityList() {
        return roleEntityList;
    }

    public void setRoleEntityList(List<SysRoleEntity> roleEntityList) {
        this.roleEntityList = roleEntityList;
    }


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_ability", joinColumns = {@JoinColumn(referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})

    @JsonIgnore
    public List<AbilityEntity> getAbilityEntityList() {
        return abilityEntityList;
    }

    public void setAbilityEntityList(List<AbilityEntity> abilityEntityList) {
        this.abilityEntityList = abilityEntityList;
    }

    @OneToMany(mappedBy = "user_matrix", cascade = CascadeType.ALL)
    @JsonIgnore
    public List<WorkerMatrixEntity> getWorkerMatrixEntities() {
        return workerMatrixEntities;
    }

    public void setWorkerMatrixEntities(List<WorkerMatrixEntity> workerMatrixEntities) {
        this.workerMatrixEntities = workerMatrixEntities;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(passwr, that.passwr) &&
                Objects.equals(salt, that.salt) &&
                Objects.equals(bonus, that.bonus) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(lev, that.lev) &&
                Objects.equals(points, that.points) &&
                Objects.equals(state, that.state) &&
                Objects.equals(roleEntityList, that.roleEntityList) &&
                Objects.equals(abilityEntityList, that.abilityEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username, passwr, salt, bonus, nickname, lev, points, state, roleEntityList, abilityEntityList);
    }
}
