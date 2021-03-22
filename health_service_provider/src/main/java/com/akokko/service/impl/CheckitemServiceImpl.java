package com.akokko.service.impl;

import com.akokko.dao.CheckitemDao;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.CheckItem;
import com.akokko.service.CheckitemService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResult pageQuary(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();

        Integer currentPage = queryPageBean.getCurrentPage();

        return null;
    }

}
