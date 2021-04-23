package com.akokko.dao;

import com.akokko.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(int userId);
}
