package com.akokko.service;

import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {
    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
