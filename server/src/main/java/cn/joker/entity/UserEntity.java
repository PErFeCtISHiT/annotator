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
@Table(name = "user", schema = "imgannotator", catalog = "")
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
    private String email;
    private List<SysRoleEntity> roleEntityList;
    private List<AbilityEntity> abilityEntityList;
    private List<BonusHistoryEntity> bonusHistoryEntityList;
    private WorkerMatrixEntity aMatrix;
    private WorkerMatrixEntity bMatrix;
    private WorkerMatrixEntity cMatrix;
    private WorkerMatrixEntity dMatrix;
    private WorkerMatrixEntity eMatrix;

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

    @Basic
    @Column(name = "email", nullable = true, length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_ability", joinColumns = {@JoinColumn(referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID")})

    @JsonIgnore
    public List<AbilityEntity> getAbilityEntityList() {
        return abilityEntityList;
    }

    public void setAbilityEntityList(List<AbilityEntity> abilityEntityList) {
        this.abilityEntityList = abilityEntityList;
    }

    @OneToMany(mappedBy = "bonusHistory_user", cascade = CascadeType.MERGE)
    @JsonIgnore
    public List<BonusHistoryEntity> getBonusHistoryEntityList() {
        return bonusHistoryEntityList;
    }

    public void setBonusHistoryEntityList(List<BonusHistoryEntity> bonusHistoryEntityList) {
        this.bonusHistoryEntityList = bonusHistoryEntityList;
    }

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.MERGE)
    @JsonIgnore
    public WorkerMatrixEntity getaMatrix() {
        return aMatrix;
    }

    public void setaMatrix(WorkerMatrixEntity aMatrix) {
        this.aMatrix = aMatrix;
    }

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.MERGE)
    @JsonIgnore
    public WorkerMatrixEntity getbMatrix() {
        return bMatrix;
    }

    public void setbMatrix(WorkerMatrixEntity bMatrix) {
        this.bMatrix = bMatrix;
    }

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.MERGE)
    @JsonIgnore
    public WorkerMatrixEntity getcMatrix() {
        return cMatrix;
    }

    public void setcMatrix(WorkerMatrixEntity cMatrix) {
        this.cMatrix = cMatrix;
    }

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.MERGE)
    @JsonIgnore
    public WorkerMatrixEntity getdMatrix() {
        return dMatrix;
    }

    public void setdMatrix(WorkerMatrixEntity dMatrix) {
        this.dMatrix = dMatrix;
    }

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.MERGE)
    @JsonIgnore
    public WorkerMatrixEntity geteMatrix() {
        return eMatrix;
    }

    public void seteMatrix(WorkerMatrixEntity eMatrix) {
        this.eMatrix = eMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                Objects.equals(email, that.email) &&
                Objects.equals(roleEntityList, that.roleEntityList) &&
                Objects.equals(abilityEntityList, that.abilityEntityList) &&
                Objects.equals(bonusHistoryEntityList, that.bonusHistoryEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username, passwr, salt, bonus, nickname, lev, points, state, email, roleEntityList, abilityEntityList, bonusHistoryEntityList);
    }
}
