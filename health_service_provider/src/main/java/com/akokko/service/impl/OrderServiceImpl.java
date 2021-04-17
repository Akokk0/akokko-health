package com.akokko.service.impl;

import com.akokko.constant.MessageConstant;
import com.akokko.dao.MemberDao;
import com.akokko.dao.OrderDao;
import com.akokko.dao.OrderSettingDao;
import com.akokko.entity.Result;
import com.akokko.pojo.Member;
import com.akokko.pojo.Order;
import com.akokko.pojo.OrderSetting;
import com.akokko.service.OrderService;
import com.akokko.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result Order(Map map) throws Exception {
        //判断之前是否进行过预约设置
        String orderDate = (String) map.get("orderDate");
        //转化为日期格式
        Date date = DateUtils.parseString2Date(orderDate);
        //调用Dao查询
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        //判断是否有进行过预约设置
        if (orderSetting == null) {
            //没有进行过预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断预约人数是否已满
        int number = orderSetting.getNumber();  //可预约人数
        int reservations = orderSetting.getReservations();  //已预约人数
        if (reservations >= number) {
            //已预约满，不可预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //判断用户是否重复预约
        String telephone = (String) map.get("telephone");
        //调用dao查询该成员
        Member member = memberDao.findByTelephone(telephone);
        //判断该成员是否存在
        if (member != null) {
            //获取参数
            Integer memberId = member.getId();
            //获取套餐id
            String setmealId = (String) map.get("setmealId");
            //封装数据
            Order order = new Order(memberId, date, Integer.parseInt(setmealId));
            //根据条件进行查询
            List<Order> list = orderDao.findByCondition(order);
            //判断是否进行预约
            if (list != null || list.size() > 0) {
                //已经进行过预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //用户还为进行注册，将自动注册为会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order(member.getId(), date, (String)map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //返回结果
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public Map findById(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
