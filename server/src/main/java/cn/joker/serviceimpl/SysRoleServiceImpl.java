package cn.joker.serviceimpl;

import cn.joker.dao.SysRoleRepository;
import cn.joker.entity.SysRoleEntity;
import cn.joker.sevice.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:09 2018/5/6
 */
@Service
@Transactional(readOnly = true,propagation = Propagation.NESTED)
@CacheConfig
public class SysRoleServiceImpl extends PubServiceImpl implements SysRoleService {

    @Autowired
    public SysRoleServiceImpl(SysRoleRepository sysRoleRepository) {
        this.repository = sysRoleRepository;
    }
}
