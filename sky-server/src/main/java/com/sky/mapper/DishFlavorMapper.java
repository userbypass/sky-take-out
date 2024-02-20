package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * @Description 插入多条菜品口味数据
     * @Date 2024/2/19 17:35
     * @Param [flavors]
     * @return void
     */
    void insert(List<DishFlavor> flavors);
    /**
     * @Description 分页查询
     * @Date 2024/2/19 20:38
     * @Param [dishPageQueryDTO]
     * @return com.github.pagehelper.Page<com.sky.vo.DishVO>
     */
    Page<DishVO> dishPageQuery(DishPageQueryDTO dishPageQueryDTO);
    /**
     * @Description 根据菜品ID批量删除口味
     * @Date 2024/2/20 15:35
     * @Param [ids]
     * @return void
     */
    void deleteByDishIds(List<Long> dishIds);
}
