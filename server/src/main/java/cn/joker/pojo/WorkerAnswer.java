package cn.joker.pojo;

import cn.joker.entity.UserEntity;

/**
 * @author: pis
 * @description: 工人标注结果
 * @date: create in 19:27 2018/5/27
 */
public class WorkerAnswer {
    private UserEntity userEntity;
    private Boolean answer;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }
}
