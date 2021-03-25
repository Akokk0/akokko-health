package com.akokko.dao;

import com.akokko.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.Map;

public interface CheckgroupDao {
    void add(CheckGroup checkGroup);

    void connectionItemAndGroup(Map map);

    Page<CheckGroup> findPageByCondition(String queryString);

    CheckGroup findById(Integer id);

    void edit(CheckGroup checkGroup);
}
