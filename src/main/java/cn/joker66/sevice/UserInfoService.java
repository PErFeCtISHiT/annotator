package cn.joker66.sevice;

import cn.joker66.entity.UserInfo;

public interface UserInfoService {
    /**通过username查找用户信息;*/
    UserInfo findByUsername(String username);
    String addUser(UserInfo userInfo);
    String modifyUser(UserInfo userInfo);
}