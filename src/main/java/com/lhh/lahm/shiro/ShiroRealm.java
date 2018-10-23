package com.lhh.lahm.shiro;

import com.alibaba.fastjson.JSON;
import com.lhh.lahm.dao.UsersMapper;
import com.lhh.lahm.entity.Users;
import com.lhh.lahm.utils.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

public class ShiroRealm extends AuthorizingRealm {

    protected Logger log = LogManager.getLogger(this.getClass());
    @Resource
    private UsersMapper usersMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取登录时输入的用户名
        // String username = (String) principals.fromRealm(getName()).iterator().next();
        // 到数据库查该用户的角色和权限信息
        // 用户肯定存在，登录认证时判断过了，其实不用判空也可以
        try {
            //String role_name= userService.findByName(username);
            log.info("角色认证："+ JSON.toJSONString(principals));
            Users user = (Users) principals.getPrimaryPrincipal();
            log.info("角色认证："+user.getRoles().getRoleName());
            // 用户肯定存在，登录认证时判断过了，其实不用判空也可以
            if (Tools.notEmpty(user.getRoles().getRoleName())) {
                // 用来存放查出的用户的所有的角色（role）及权限（permission）
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                info.addRole(user.getRoles().getRoleName());
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果查询不到用户, 返回空, Shiro会抛出UnknownAccountException异常,自动跳转到unauthorizedUrl指定的地址
        return null;
    }
    /**
     * 登录认证时调用，用法如下： Session currentUser = subject.getSession(false);
     * UsernamePasswordToken token = new UsernamePasswordToken(username,
     * password); currentUser.login(token); currentUser.isAuthenticated()；
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal(); // 得到用户名
        String password = new String((char[]) authenticationToken.getCredentials()); // 得到密码
        // 判断用户名密码是否正确（根据用户名和密码查询，查得到说明用户名和密码正确）)
        try {
            //获取用户信息
            Users user=usersMapper.loginFind(username, password);
            log.info("用户认证："+user.getUserName()+"-"+user.getRoles().getRoleName());
            // 若用户存在，更新登陆时间。
            if (user!=null) {

                getShiroSession().setAttribute("sessionUser", user);

                return new SimpleAuthenticationInfo(user, password, getName());
                //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将此用户存放到登录认证info中
        return null;
    }

    /**
     * 权限认证时调用，用法如下: 1、在jsp页面使用shiro标签后，进入标签会自动调用 2、手动显示调用 3、使用注解自动调用，如：只有拥有
     * user:create权限，才能进行注册
     *
     * @RequestMapping(value = "/register") @RequiresPermissions("user:create")
     *                       public boolean register(User user){ return
     *                       userService.register(user); }
     */



    // 清除所有用户授权信息缓存
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
    protected Session getShiroSession() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session;
    }

}
