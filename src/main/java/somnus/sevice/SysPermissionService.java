package somnus.sevice;

import somnus.entity.SysPermission;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:25 2018/3/28
 */
public interface SysPermissionService {
    SysPermission findBySysPermissionId(String spid);
}
