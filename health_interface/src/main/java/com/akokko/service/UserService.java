package com.akokko.service;

import com.akokko.pojo.User;

public interface UserService {
    User findByUsername(String username);
}
