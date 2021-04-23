package com.akokko.dao;

import com.akokko.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(int roleId);
}
