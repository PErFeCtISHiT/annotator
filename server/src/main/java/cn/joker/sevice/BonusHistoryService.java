package cn.joker.sevice;

import cn.joker.entity.BonusHistoryEntity;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:52 2018/4/13
 */
public interface BonusHistoryService extends PubService {
    /**
     *
     * @param username 用户名
     * @return 查找符合的结果
     */
    List<BonusHistoryEntity> findByName(String username);
}
