package cn.joker.config;

import cn.joker.dao.UserRepository;
import cn.joker.entity.SysPermissionEntity;
import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.UserEntity;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    /**
     * @author:pis
     * @description: 权限信息集合
     * @date: 10:15 2018/3/29
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserEntity userEntity = (UserEntity) principals.getPrimaryPrincipal();
        for (SysRoleEntity role : userEntity.getRoleEntityList()) {
            authorizationInfo.addRole(role.getRole());
            for (SysPermissionEntity p : role.getSysPermissionEntityList()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        //通过username从database中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(
                userEntity.getUsername(), //用户名
                userEntity.getPasswr(), //密码
                ByteSource.Util.bytes(userEntity.getUsername() + userEntity.getSalt()),//salt=username+salt
                getName()  //realm name
        );
    }

}