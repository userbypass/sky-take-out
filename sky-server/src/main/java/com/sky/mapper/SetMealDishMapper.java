package com.sky.mapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * @Description 根据一组菜品id返回重复的套餐id
     * @Date 2024/2/20 15:11
     * @Param [ids]
     * @return java.lang.Integer
     */
    List<Long> selectByDishIds(List<Long> ids);
}
