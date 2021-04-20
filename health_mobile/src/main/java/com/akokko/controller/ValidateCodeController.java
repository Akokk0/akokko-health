package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.constant.RedisMessageConstant;
import com.akokko.entity.Result;
import com.akokko.utils.SMSUtils;
import com.akokko.utils.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成6位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //调用阿里云方法发送短信验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //短信验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //短信验证码发送成功,将验证码存入redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, code.toString());
        //返回成功结果
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //生成6位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //调用阿里云方法发送短信验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //短信验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //短信验证码发送成功,将验证码存入redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 300, code.toString());
        //返回成功结果
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
