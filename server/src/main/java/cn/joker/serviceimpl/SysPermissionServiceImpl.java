package cn.joker.serviceimpl;

import cn.joker.dao.SysPermissionRepository;
import cn.joker.sevice.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:22 2018/5/6
 */
@Service
public class SysPermissionServiceImpl extends PubServiceImpl implements SysPermissionService {

    @Autowired
    public SysPermissionServiceImpl(SysPermissionRepository sysPermissionRepository) {
        this.repository = sysPermissionRepository;
    }

}
