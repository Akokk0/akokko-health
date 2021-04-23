package com.akokko.service;

import com.akokko.pojo.Permission;
import com.akokko.pojo.Role;
import com.akokko.pojo.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用service根据Username查询user对象
        User user = userService.findByUsername(username);
        //判断用户是否存在
        if (user == null) {
            //用户名输入错误
            return null;
        }
        //获取参数
        Set<Role> roles = user.getRoles();
        //创建权限集合
        List<GrantedAuthority> list = new ArrayList<>();
        //动态为该用户赋予权限
        for (Role role : roles) {
            //赋予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取权限集合
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //赋予权限
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //创建user对象并返回
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }
}
