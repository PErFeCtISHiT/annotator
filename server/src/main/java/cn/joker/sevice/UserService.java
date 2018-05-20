package cn.joker.sevice;

import cn.joker.entity.UserEntity;

import java.util.List;

public interface UserService extends PubService{
    UserEntity findByUsername(String username);

}