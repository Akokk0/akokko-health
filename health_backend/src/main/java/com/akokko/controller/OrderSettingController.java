package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.entity.Result;
import com.akokko.pojo.OrderSetting;
import com.akokko.service.OrderSettingService;
import com.akokko.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            //将Excel表格转换为List集合
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //创建一个List集合
            List<OrderSetting> data = new ArrayList<>();
            //遍历转换为List<OrderSetting>
            for (String[] string : strings) {
                OrderSetting orderSetting = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
                //将预约设置项添加到List集合中
                data.add(orderSetting);
                //调用Service方法
                orderSettingService.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrdersettingByMonth")
    public Result getOrdersettingByMonth(String date) {
        try {
            //调用Service查询得到集合
            List<Map> list = orderSettingService.getOrdersettingByMonth(date);
            //返回值
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
