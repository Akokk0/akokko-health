package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.entity.Result;
import com.akokko.pojo.Member;
import com.akokko.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        //获取参数
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String validateCodeInRedis = jedisPool.getResource().get("telephone + RedisMessageConstant.SENDTYPE_LOGIN");
        //判断验证码是否正确
        if (validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            //验证码输入正确，判断用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            //判断该用户是否注册
            if (member == null) {
                //用户尚未注册，自动注册
                member = new Member();  //防止空指针异常
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //将用户信息保存到Cookie当中，追踪用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");  //设置路径
            cookie.setMaxAge(60 * 60 * 24 * 30);  //设置存活时间
            response.addCookie(cookie);
            //将用户信息保存到redis当中
            String json_member = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60 * 30, json_member);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
