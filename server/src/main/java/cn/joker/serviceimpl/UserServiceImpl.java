package cn.joker.serviceimpl;

import cn.joker.dao.UserRepository;
import cn.joker.entity.UserEntity;
import cn.joker.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:18 2018/5/6
 */
@Service
public class UserServiceImpl extends PubServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> findAllWorkers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserEntity> ret = new ArrayList<>();
        for(UserEntity userEntity : userEntities){
            if(userEntity.getRoleEntityList().get(0).getId() == 4){
                ret.add(userEntity);
            }
        }
        return ret;
    }

}
