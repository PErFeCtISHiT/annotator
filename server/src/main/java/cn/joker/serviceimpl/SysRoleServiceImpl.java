package cn.joker.serviceimpl;

import cn.joker.dao.SysRoleDao;
import cn.joker.entity.SysRole;
import cn.joker.sevice.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/3/28
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public SysRole findBySysRoleId(String srid) {
        return sysRoleDao.findBySysRoleId(srid);
    }
}
