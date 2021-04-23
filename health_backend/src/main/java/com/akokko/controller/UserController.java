package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername")
    public Result getUsername() {
        //通过security上下文应用获取用户对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //判断是否获取到用户对象
        if (user == null) {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
        //获取用户名并返回
        String username = user.getUsername();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }
}
