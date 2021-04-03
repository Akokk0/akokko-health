package com.akokko.service.impl;

import com.akokko.constant.RedisConstant;
import com.akokko.dao.SetmealDao;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.Setmeal;
import com.akokko.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //将套餐信息写入到数据库
        setmealDao.add(setmeal);
        //获取存入setmeal的id
        Integer setmealId = setmeal.getId();
        //调用本类方法将套餐与检查组进行连接
        this.connectionSetmealAndCheckgroup(setmealId, checkgroupIds);
        //将文件名称写入到redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //开启分页助手
        PageHelper.startPage(currentPage, pageSize);
        //调用Dao
        Page<Setmeal> page = setmealDao.findPage(queryString);
        //返回值
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Integer> findGroupBySetmeal(Integer id) {
        return setmealDao.findGroupBySetmeal(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {

    }

    @Override
    public void delete(Integer id) {

    }

    /**
     * 将套餐与检查组进行绑定
     * @param setmealId
     * @param checkgroupIds
     */
    public void connectionSetmealAndCheckgroup(Integer setmealId, Integer[] checkgroupIds) {
        //判断检查组勾选是否为空，为空则不写入数据库
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //将套餐与检查组关联
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.connectionSetmealAndCheckgroup(map);
            }
        } else {
            throw new RuntimeException();
        }
    }
}
