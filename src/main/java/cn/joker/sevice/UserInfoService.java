package cn.joker.sevice;

import cn.joker.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    UserInfo findByUsername(String username);

    boolean addUser(UserInfo userInfo);

    boolean modifyUser(UserInfo userInfo);

    List<UserInfo> findAllUser();

    boolean deleteUser(String username);
}