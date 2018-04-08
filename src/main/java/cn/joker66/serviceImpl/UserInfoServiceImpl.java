package cn.joker66.serviceImpl;

import org.springframework.stereotype.Service;
import cn.joker66.dao.UserInfoDao;
import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.UserInfoService;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private UserInfoDao userInfoDao = new UserInfoDao();
    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername():" + username);
        return userInfoDao.findByUsername(username);
    }
}