package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * @return void
     * @Description 插入多条菜品口味数据
     * @Date 2024/2/19 17:35
     * @Param [flavors]
     */
    void insert(List<DishFlavor> flavors);

    /**
     * @return com.github.pagehelper.Page<com.sky.vo.DishVO>
     * @Description 分页查询
     * @Date 2024/2/19 20:38
     * @Param [dishPageQueryDTO]
     */
    Page<DishVO> dishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @return void
     * @Description 根据菜品ID批量删除口味
     * @Date 2024/2/20 15:35
     * @Param [ids"s
     */
    void deleteByDishIds(List<Long> dishIds);

    @Select("select * from sky_take_out.dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> selectByDishId(Long dishId);
}

