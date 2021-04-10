package com.akokko.service;

import com.akokko.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> data);
    List<Map> getOrdersettingByMonth(String date);
    void editNumberByDate(OrderSetting orderSetting);
}
