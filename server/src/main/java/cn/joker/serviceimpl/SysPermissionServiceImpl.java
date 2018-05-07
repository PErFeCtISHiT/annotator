package cn.joker.serviceimpl;

import cn.joker.dao.SysPermissionRepository;
import cn.joker.entity.SysPermissionEntity;
import cn.joker.sevice.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:22 2018/5/6
 */
public class SysPermissionServiceImpl implements SysPermissionService{
    @Autowired
    private SysPermissionRepository sysPermissionRepository;
    @Override
    public SysPermissionEntity findBySysPermissionId(Integer spid) {
        return sysPermissionRepository.findById(spid);
    }
}
