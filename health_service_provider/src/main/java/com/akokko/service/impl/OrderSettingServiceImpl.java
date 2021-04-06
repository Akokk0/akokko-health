package com.akokko.service.impl;

import com.akokko.dao.OrderSettingDao;
import com.akokko.pojo.OrderSetting;
import com.akokko.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
