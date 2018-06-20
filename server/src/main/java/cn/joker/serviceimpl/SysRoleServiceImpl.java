package cn.joker.serviceimpl;

import cn.joker.dao.SysRoleRepository;
import cn.joker.sevice.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:09 2018/5/6
 */
@Service
public class SysRoleServiceImpl extends PubServiceImpl implements SysRoleService {

    @Autowired
    public SysRoleServiceImpl(SysRoleRepository sysRoleRepository) {
        this.repository = sysRoleRepository;
    }
}
