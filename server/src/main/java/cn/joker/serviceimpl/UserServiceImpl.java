package cn.joker.serviceimpl;

import cn.joker.dao.UserRepository;
import cn.joker.entity.UserEntity;
import cn.joker.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:18 2018/5/6
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean addUser(UserEntity userEntity) {
        return userRepository.save(userEntity) != null;
    }

    @Override
    public boolean modifyUser(UserEntity userEntity) {
        return userRepository.save(userEntity) == null;
    }

    @Override
    public List<UserEntity> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        userRepository.delete(userEntity);
        return true;
    }
}
