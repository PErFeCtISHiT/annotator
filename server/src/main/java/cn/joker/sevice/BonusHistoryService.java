package cn.joker.sevice;

import cn.joker.entity.BonusHistory;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:52 2018/4/13
 */
public interface BonusHistoryService {
    boolean addBonusHistory(BonusHistory bonusHistory);

    List<BonusHistory> findByName(String username);
}
