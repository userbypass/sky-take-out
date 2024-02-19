package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    /**
     * @Description 新增菜品及相应口味
     * @Date 2024/2/19 10:49
     * @Param [dishDTO]
     * @return void
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //插入1条菜品表数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        //插入多条口味表数据

    }
}
