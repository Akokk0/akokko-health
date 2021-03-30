package com.akokko.dao;

import com.akokko.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void connectionSetmealAndCheckgroup(Map map);

    Page<Setmeal> findPage(String queryString);

}
