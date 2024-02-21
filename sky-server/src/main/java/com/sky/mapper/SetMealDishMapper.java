package com.sky.mapper;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    /**
     * @Description 根据套餐ID查询对应的菜品信息
     * @Date 2024/2/21 11:48
     * @Param [setmealId]
     * @return java.util.List<com.sky.entity.SetmealDish>
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
