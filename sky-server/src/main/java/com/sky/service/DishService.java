package com.sky.service;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.enumeration.OperationType;

public interface DishService {
    /**
     * @Description 新增菜品及相应口味
     * @Date 2024/2/19 10:49
     * @Param [dishDTO]
     * @return void
     */
    void saveWithFlavor(DishDTO dishDTO);
}
