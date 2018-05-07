package cn.joker.serviceimpl;

import cn.joker.dao.SysRoleRepository;
import cn.joker.entity.SysRoleEntity;
import cn.joker.sevice.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:09 2018/5/6
 */
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Override
    public SysRoleEntity findBySysRoleId(Integer srid) {
        return sysRoleRepository.findById(srid);
    }
}
