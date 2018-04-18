package cn.joker.serviceimpl;

import cn.joker.dao.BonusHistoryDao;
import cn.joker.entity.BonusHistory;
import cn.joker.sevice.BonusHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:57 2018/4/13
 */
@Service
public class BonusHistoryServiceImpl implements BonusHistoryService {
    @Resource
    private BonusHistoryDao bonusHistoryDao;

    @Override
    public boolean addBonusHistory(BonusHistory bonusHistory) {
        return bonusHistoryDao.addBonusHistory(bonusHistory);
    }

    @Override
    public List<BonusHistory> findByName(String username) {
        return bonusHistoryDao.findByName(username);
    }
}
