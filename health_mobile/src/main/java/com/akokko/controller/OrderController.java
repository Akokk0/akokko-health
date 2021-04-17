package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.constant.RedisMessageConstant;
import com.akokko.entity.Result;
import com.akokko.pojo.Order;
import com.akokko.service.OrderService;
import com.akokko.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //获取输入的手机号码和验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //获取存储在redis里的验证码
        String validateInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //比对验证码是否一致
        if (validateInRedis != null && validateCode != null && validateCode.equals(validateInRedis)) {
            //验证成功，调用Service
            Result result = null;
            try {
                //设置预约类型
                map.put("orderType", Order.ORDERTYPE_WEIXIN);
                result = orderService.Order(map);
            } catch (Exception e) {
                //调用Service失败
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                //调用Service成功，发送短信通知
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            //验证失败，返回结果
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
