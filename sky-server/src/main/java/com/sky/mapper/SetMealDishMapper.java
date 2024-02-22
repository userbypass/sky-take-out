package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * @return java.lang.Integer
     * @Description 根据一组菜品id返回重复的套餐id
     * @Date 2024/2/20 15:11
     * @Param [ids]
     */
    List<Long> selectByDishIds(List<Long> ids);

    /**
     * @return java.util.List<com.sky.entity.SetmealDish>
     * @Description 根据套餐ID查询包含的菜品信息
     * @Date 2024/2/21 11:48
     * @Param [setmealId]
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * @return void
     * @Description 根据套餐ID删除套餐-菜品对应关系
     * @Date 2024/2/21 12:55
     * @Param [id]
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * @return void
     * @Description 批量插入套餐-菜品对应关系
     * @Date 2024/2/21 13:08
     * @Param [setmealDishes]
     */
    void insertBatch(List<SetmealDish> setmealDishes, Long setmealId);

}
