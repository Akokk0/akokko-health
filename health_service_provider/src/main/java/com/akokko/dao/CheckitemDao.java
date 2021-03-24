package com.akokko.dao;

import com.akokko.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckitemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

    Long findCheckitemById(Integer id);

    void deleteById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
