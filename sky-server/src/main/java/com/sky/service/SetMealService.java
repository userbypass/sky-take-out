package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO);

    SetmealVO getWithDish(Long id);

    void updateWithDish(SetmealDTO setmealDTO);

    void updateStatus(Integer status, Long id);

    void addWithDishes(SetmealDTO setmealDTO);

    void deleteBatchWithDishes(List<Long> ids);
}
