package com.akokko.service;

import com.akokko.entity.Result;
import java.util.Map;

public interface OrderService {
    Result Order(Map map) throws Exception;
    Map findById(Integer id);
}
