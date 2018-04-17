package cn.joker.serviceimpl;

import cn.joker.dao.SysPermissionDao;
import cn.joker.entity.SysPermission;
import cn.joker.sevice.SysPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/3/28
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Resource
    private SysPermissionDao sysPermissionDao;

    @Override
    public SysPermission findBySysPermissionId(String spid) {
        return sysPermissionDao.findBySysPermissionId(spid);
    }
}
