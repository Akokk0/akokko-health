package com.akokko.service.impl;

import com.akokko.dao.CheckitemDao;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.CheckItem;
import com.akokko.service.CheckitemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckitemService.class)
@Transactional
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    private CheckitemDao checkitemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkitemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        //开启分页助手
        PageHelper.startPage(currentPage, pageSize);

        //调用dao从数据库获取数据
        Page<CheckItem> page = checkitemDao.findPage(queryString);

        //从Page对象中拿取数据
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();

        return new PageResult(total, rows);
    }

    @Override
    public void deleteById(Integer id) {
        //判断该检查项是否包含在检查组里
        Long count = checkitemDao.findCheckitemById(id);
        if (count > 0) new RuntimeException();  //说明该检查项包含在检查组里

        //没有进到if里说明没有检查组包含该检查项
        checkitemDao.deleteById(id);
    }
}
