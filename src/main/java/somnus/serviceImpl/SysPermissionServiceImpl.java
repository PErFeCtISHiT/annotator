package somnus.serviceImpl;

import org.springframework.stereotype.Service;
import somnus.dao.SysPermissionDao;
import somnus.entity.SysPermission;
import somnus.sevice.SysPermissionService;

import javax.annotation.Resource;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/3/28
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService{
    @Resource
    private SysPermissionDao sysPermissionDao = new SysPermissionDao();
    @Override
    public SysPermission findBySysPermissionId(String spid) {
        return sysPermissionDao.findBySysPermissionId(spid);
    }
}
