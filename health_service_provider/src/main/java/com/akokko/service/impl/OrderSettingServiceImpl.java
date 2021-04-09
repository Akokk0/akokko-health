package com.akokko.service.impl;

import com.akokko.dao.OrderSettingDao;
import com.akokko.pojo.OrderSetting;
import com.akokko.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> data) {
        if (data != null && data.size() > 0) {
            //遍历集合
            for (OrderSetting ordersetting : data) {
                //先判断日期是否已经被写入
                long count = orderSettingDao.CountByOrderDate(ordersetting.getOrderDate());
                if (count > 0) {  //日期被写入
                    orderSettingDao.editNumberByOrderDate(ordersetting);
                } else {  //日期未被写入
                    orderSettingDao.add(ordersetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrdersettingByMonth(String date) {
        //初始化数据
        String begin = date + "-1";
        String end = date + "-31";
        //封装数据
        Map<String,String> data = new HashMap<>();
        data.put("begin", begin);
        data.put("end", end);
        //调用Dao查询
        List<OrderSetting> values = orderSettingDao.getOrdersettingByMonth(data);
        //遍历集合,存入数据
        List<Map> result = new ArrayList<>();
        if (values != null && values.size() > 0) {
            for (OrderSetting orderSetting : values) {
                //封装数据
                Map<String,Object> tempValue = new HashMap<>();
                tempValue.put("date", orderSetting.getOrderDate().getDate());
                tempValue.put("number", orderSetting.getNumber());
                tempValue.put("reservations", orderSetting.getReservations());
                //存入数据
                result.add(tempValue);
            }
        }
        return result;
    }
}
