package com.akokko.dao;

import com.akokko.pojo.OrderSetting;

import java.util.Date;

public interface OrderSettingDao {
    long CountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderDate);

    void add(OrderSetting orderSetting);
}
