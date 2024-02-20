package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    /**
     * @Description 新增菜品及相应口味
     * @Date 2024/2/19 10:49
     * @Param [dishDTO]
     * @return void
     */
    void saveWithFlavor(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    void updateStatus(Integer status,Long id);
}
