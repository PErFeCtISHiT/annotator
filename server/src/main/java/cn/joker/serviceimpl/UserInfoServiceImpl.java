package cn.joker.serviceimpl;

import cn.joker.dao.UserInfoDao;
import cn.joker.entity.UserInfo;
import cn.joker.sevice.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;

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