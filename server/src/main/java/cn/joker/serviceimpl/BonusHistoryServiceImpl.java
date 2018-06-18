package cn.joker.serviceimpl;

import cn.joker.dao.BonusHistoryRepository;
import cn.joker.entity.BonusHistoryEntity;
import cn.joker.sevice.BonusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 7:22 2018/5/18
 */
@Service
public class BonusHistoryServiceImpl extends PubServiceImpl implements BonusHistoryService {
    private BonusHistoryRepository bonusHistoryRepository;

    /**
     *
     * @param bonusHistoryRepository 历史
     */
    @Autowired
    public BonusHistoryServiceImpl(BonusHistoryRepository bonusHistoryRepository) {
        this.repository = bonusHistoryRepository;
        this.bonusHistoryRepository = bonusHistoryRepository;
    }

    /**
     *
     * @param username 用户名
     * @return
     */
    @Override
    public List<BonusHistoryEntity> findByName(String username) {
        List<BonusHistoryEntity> bonusHistoryEntities = bonusHistoryRepository.findAll();
        List<BonusHistoryEntity> ret = new ArrayList<>();
        for (BonusHistoryEntity bonusHistoryEntity : bonusHistoryEntities) {
            if (bonusHistoryEntity.getBonusHistory_user().getUsername().equals(username))
                ret.add(bonusHistoryEntity);
        }
        return ret;
    }
}
