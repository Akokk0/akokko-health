package com.akokko.service.impl;

import com.akokko.dao.CheckgroupDao;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.CheckGroup;
import com.akokko.service.CheckgroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupDao checkgroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //判断是否有选中的检查项，若没有则报错
        if (checkitemIds != null && checkitemIds.length > 0) {
            //将检查组信息写入到t_checkgroup
            checkgroupDao.add(checkGroup);
            //获取刚添加进数据库的id值
            Integer checkgroupId = checkGroup.getId();
            //遍历循环将检查项与检查组绑定
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkitemId", checkitemId);
                map.put("checkgroupId", checkgroupId);
                checkgroupDao.connectionItemAndGroup(map);
            }
        } else {
            new RuntimeException();
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        //开启分页助手
        PageHelper.startPage(currentPage, pageSize);

        //调用dao进行查询
        Page<CheckGroup> page = checkgroupDao.findPageByCondition(queryString);

        //返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }
}
