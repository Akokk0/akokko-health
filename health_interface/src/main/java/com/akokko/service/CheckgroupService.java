package com.akokko.service;

import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.CheckGroup;

public interface CheckgroupService {

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    void edit(CheckGroup checkGroup);

}
