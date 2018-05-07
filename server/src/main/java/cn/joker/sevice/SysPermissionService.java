package cn.joker.sevice;

import cn.joker.entity.SysPermissionEntity;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:25 2018/3/28
 */
public interface SysPermissionService {
    SysPermissionEntity findBySysPermissionId(Integer spid);
}
