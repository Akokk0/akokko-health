package com.akokko.dao;

import com.akokko.pojo.CheckItem;
import com.github.pagehelper.Page;

public interface CheckitemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

}
