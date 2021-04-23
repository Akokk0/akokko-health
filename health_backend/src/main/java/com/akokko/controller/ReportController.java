package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.entity.Result;
import com.akokko.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            //创建日历对象
            Calendar calendar = Calendar.getInstance();
            //获得去年的日期
            calendar.add(Calendar.MONTH, -12);
            //创建日期集合
            List<String> date = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                //添加时间
                calendar.add(Calendar.MONTH, 1);
                //将时间存入集合
                date.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }
            //调用service查询人数
            List<Integer> memberCountByMonth = memberService.findMemberCountByMonth(date);
            //创建集合添加数据
            Map map = new HashMap();
            map.put("months", date);
            map.put("memberCount", memberCountByMonth);
            //返回结果
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}
