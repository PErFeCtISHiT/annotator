package cn.joker66.serviceImpl;

import cn.joker66.dao.UserInfoDao;
import cn.joker66.entity.UserInfo;
import cn.joker66.sevice.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao ;

    @Override
    public UserInfo findByUsername(String username) {
        return userInfoDao.findByUsername(username);
    }

    @Override
    public boolean addUser(UserInfo userInfo) {
        return userInfoDao.addUser(userInfo);
    }

    @Override
    public boolean modifyUser(UserInfo userInfo) {
        return userInfoDao.modifyUser(userInfo);
    }

    @Override
    public List<UserInfo> findAllUser() {
        return userInfoDao.findAllUser();
    }

    @Override
    public boolean deleteUser(String username) {
        return userInfoDao.deleteUser(username);
    }

}