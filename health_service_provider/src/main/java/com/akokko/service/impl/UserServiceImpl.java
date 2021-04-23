package com.akokko.service.impl;

import com.akokko.dao.PermissionDao;
import com.akokko.dao.RoleDao;
import com.akokko.dao.UserDao;
import com.akokko.pojo.Permission;
import com.akokko.pojo.Role;
import com.akokko.pojo.User;
import com.akokko.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        //通过dao查询用户
        User user = userDao.findByUsername(username);
        //判断用户是否存在
        if (user == null) {
            //用户不存在
            return null;
        }
        //用户存在，获取用户id
        Integer userId = user.getId();
        //通过dao查询该用户具有的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        //判断role是否为空，为空则不进行遍历
        if (roles != null && roles.size() > 0) {
            //遍历每个角色
            for (Role role : roles) {
                //获取角色id
                Integer roleId = role.getId();
                //通过角色id查询dao
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                //判断权限集合是否为空，为空则不储存
                if (permissions != null && permissions.size() > 0) {
                    //将权限存入角色对象
                    role.setPermissions(permissions);
                }
            }
        }
        //将角色存入User对象
        user.setRoles(roles);
        //返回User对象
        return user;
    }
}
