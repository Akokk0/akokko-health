package com.akokko.dao;

import com.akokko.pojo.User;

public interface UserDao {
    User findByUsername(String username);
}
