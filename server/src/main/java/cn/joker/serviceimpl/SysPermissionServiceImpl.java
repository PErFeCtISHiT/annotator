package cn.joker.serviceimpl;

import cn.joker.dao.SysPermissionRepository;
import cn.joker.entity.SysPermissionEntity;
import cn.joker.sevice.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:22 2018/5/6
 */
@Service
@Transactional(readOnly = true,propagation = Propagation.NESTED)
@CacheConfig
public class SysPermissionServiceImpl extends PubServiceImpl implements SysPermissionService{
    private final SysPermissionRepository sysPermissionRepository;

    @Autowired
    public SysPermissionServiceImpl(SysPermissionRepository sysPermissionRepository) {
        this.repository = sysPermissionRepository;
        this.sysPermissionRepository = sysPermissionRepository;
    }

}
