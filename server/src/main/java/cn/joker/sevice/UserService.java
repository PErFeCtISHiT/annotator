package cn.joker.sevice;

import cn.joker.entity.UserEntity;

import java.util.List;

public interface UserService extends PubService {
    /**
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserEntity findByUsername(String username);

    /**
     *
     * @return 用户列表
     */
    List<UserEntity> findAll();

    /**
     *
     * @return 工人列表
     */
    List<UserEntity> findAllWorkers();
}