package com.akokko.dao;

import com.akokko.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    long CountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderDate);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrdersettingByMonth(Map data);
}
