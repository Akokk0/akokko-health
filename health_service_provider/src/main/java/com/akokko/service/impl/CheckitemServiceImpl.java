package com.akokko.service.impl;

import com.akokko.dao.CheckitemDao;
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
}
