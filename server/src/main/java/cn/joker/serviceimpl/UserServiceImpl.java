package cn.joker.serviceimpl;

import cn.joker.dao.UserRepository;
import cn.joker.entity.UserEntity;
import cn.joker.sevice.UserService;
import cn.joker.util.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:18 2018/5/6
 */
@Service
@Transactional(readOnly = true,propagation = Propagation.NESTED)
@CacheConfig
public class UserServiceImpl extends PubServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
