package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * @return java.lang.Integer
     * @Description 根据分类id查询菜品数量
     * @Date 2024/2/19 17:28
     * @Param [categoryId]
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

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
     * @Description 根据ID查询菜品信息
     * @Date 2024/2/20 15:00
     * @Param [id]
     */
    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);

    /**
     * @return void
     * @Description 根据ID批量删除菜品
     * @Date 2024/2/20 15:25
     * @Param [ids]
     */
    void deleteByIds(List<Long> ids);

    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Long id);
}
