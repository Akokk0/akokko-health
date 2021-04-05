package com.akokko.controller;

import com.akokko.constant.MessageConstant;
import com.akokko.constant.RedisConstant;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.entity.Result;
import com.akokko.pojo.Setmeal;
import com.akokko.service.SetmealService;
import com.akokko.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String extention = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID().toString() + extention;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            if (checkgroupIds == null || checkgroupIds.length == 0) return new Result(false, MessageConstant.CHECK_CHECKGROUP_NULL);
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }

    @RequestMapping("/findById")
    public Setmeal findById(Integer id) {
        return setmealService.findById(id);
    }

    @RequestMapping("/findGroupBySetmeal")
    public Result findGroupBySetmeal(Integer id) {
        try {
            List<Integer> list = setmealService.findGroupBySetmeal(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            if (checkgroupIds != null && checkgroupIds.length > 0) return new Result(false, MessageConstant.CHECK_CHECKGROUP_NULL);
            setmealService.edit(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_SUCCESS);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_FAIL);
    }
}
