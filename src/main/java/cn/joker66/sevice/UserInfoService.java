package cn.joker66.sevice;

import cn.joker66.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    UserInfo findByUsername(String username);

    boolean addUser(UserInfo userInfo);

    boolean modifyUser(UserInfo userInfo);

    List<UserInfo> findAllUser();

    boolean deleteUser(String username);
}