package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.entity.Result;
import com.akokko.pojo.CheckItem;
import com.akokko.service.CheckitemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkitem")
public class CheckitemController {

    @Reference
    private CheckitemService checkitemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkitemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkitemService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkitemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
