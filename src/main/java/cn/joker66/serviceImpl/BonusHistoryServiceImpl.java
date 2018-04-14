package cn.joker66.serviceImpl;

import cn.joker66.dao.BonusHistoryDao;
import cn.joker66.entity.BonusHistory;
import cn.joker66.sevice.BonusHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:57 2018/4/13
 */
@Service
public class BonusHistoryServiceImpl implements BonusHistoryService {
    @Resource
    private BonusHistoryDao bonusHistoryDao ;

    @Override
    public boolean addBonusHistory(BonusHistory bonusHistory) {
        return bonusHistoryDao.addBonusHistory(bonusHistory);
    }
}
