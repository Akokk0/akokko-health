package com.akokko.dao;

import com.akokko.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckgroupDao {
    void add(CheckGroup checkGroup);

    void connectionItemAndGroup(Map map);

    Page<CheckGroup> findPageByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findItemByGroup(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    void deleteGroup(Integer id);

    List<CheckGroup> findAll();
}
