package com.akokko.dao;

import com.akokko.pojo.CheckGroup;

import java.util.Map;

public interface CheckgroupDao {
    void add(CheckGroup checkGroup);

    void connectionItemAndGroup(Map<String,Integer> map);
}
