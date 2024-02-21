package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * @return java.lang.Integer
     * @Description 根据分类id查询菜品
     * @Date 2024/2/19 17:28
     * @Param [categoryId]
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> selectByCategoryId(Long categoryId);

    /**
     * @return java.util.List<com.sky.entity.Dish>
     * @Description 根据菜品名称查询菜品
     * @Date 2024/2/21 14:09
     * @Param [dishName]
     */
    @Select("select * from dish where name like concat('%',#{dishName},'%')")
    List<Dish> selectByDishName(String dishName);

    /**
     * @return void
     * @Description 新增菜品
     * @Date 2024/2/19 17:20
     * @Param [dish]
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * @return com.sky.entity.Dish
     * @Description 根据菜品ID查询菜品信息
     * @Date 2024/2/20 15:00
     * @Param [id]
     */
    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);

    /**
     * @return void
     * @Description 根据菜品ID批量删除菜品
     * @Date 2024/2/20 15:25
     * @Param [ids]
     */
    void deleteByIds(List<Long> ids);

    /**
     * @return void
     * @Description 修改菜品启用状态
     * @Date 2024/2/20 19:55
     * @Param [status, id]
     */
    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Long id);

    /**
     * @return void
     * @Description 更新菜品信息
     * @Date 2024/2/20 19:55
     * @Param [dish]
     */
    @AutoFill(OperationType.UPDATE)
    void updateById(Dish dish);

}
