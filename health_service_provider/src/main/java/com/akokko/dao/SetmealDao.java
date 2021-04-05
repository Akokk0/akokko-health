package com.akokko.dao;

import com.akokko.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void connectionSetmealAndCheckgroup(Map map);

    Page<Setmeal> findPage(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findGroupBySetmeal(Integer id);

    void edit(Setmeal setmeal);

    void deleteCheckgroupIds(Integer id);

    void delete(Integer id);

}
