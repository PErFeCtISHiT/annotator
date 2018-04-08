package somnus.serviceImpl;

import org.springframework.stereotype.Service;
import somnus.dao.SysRoleDao;
import somnus.entity.SysRole;
import somnus.sevice.SysRoleService;

import javax.annotation.Resource;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/3/28
 */
@Service
public class SysRoleServiceImpl implements SysRoleService{
    @Resource
    private SysRoleDao sysRoleDao = new SysRoleDao();
    @Override
    public SysRole findBySysRoleId(String srid) {
        return sysRoleDao.findBySysRoleId(srid);
    }
}
