package cn.joker66.serviceImpl;

import cn.joker66.dao.UserInfoDao;
import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private UserInfoDao userInfoDao = new UserInfoDao();
    @Override
    public UserInfo findByUsername(String username) {
        return userInfoDao.findByUsername(username);
    }

    @Override
    public String addUser(UserInfo userInfo) {
        return userInfoDao.add(userInfo);
    }

    @Override
    public String modifyUser(UserInfo userInfo) {
        return userInfoDao.modifyUser(userInfo);
    }

}