package somnus.serviceImpl;

import org.springframework.stereotype.Service;
import somnus.dao.UserInfoDao;
import somnus.entity.UserInfo;
import somnus.sevice.UserInfoService;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername():" + username);
        return userInfoDao.findByUsername(username);
    }
}