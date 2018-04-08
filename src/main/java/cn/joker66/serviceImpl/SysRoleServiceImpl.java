package cn.joker66.serviceImpl;

import org.springframework.stereotype.Service;
import cn.joker66.dao.SysRoleDao;
import cn.joker66.entity.SysRole;
import cn.joker66.sevice.SysRoleService;

import javax.annotation.Resource;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/3/28
 */
@Service
public class SysRoleServiceImpl implements SysRoleService{
    private SysRoleDao sysRoleDao = new SysRoleDao();
    @Override
    public SysRole findBySysRoleId(String srid) {
        return sysRoleDao.findBySysRoleId(srid);
    }
}
