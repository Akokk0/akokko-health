package com.akokko.service;

import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.CheckItem;

public interface CheckitemService {
    void add(CheckItem checkItem);

    PageResult pageQuary(QueryPageBean queryPageBean);
}
