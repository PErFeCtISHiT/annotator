package cn.joker.sevice;

import cn.joker.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity findByUsername(String username);

    boolean addUser(UserEntity userEntity);

    boolean modifyUser(UserEntity userEntity);

    List<UserEntity> findAllUser();

    boolean deleteUser(String username);
}