package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * @return com.github.pagehelper.Page<com.sky.entity.Setmeal>
     * @Description 分页查询
     * @Date 2024/2/21 10:28
     * @Param [pageQueryDTO]
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO pageQueryDTO);
    /**
     * @Description 根据套餐ID查询套餐信息
     * @Date 2024/2/21 11:38
     * @Param [setmealId]
     * @return com.sky.entity.Setmeal
     */
    @Select("select * from setmeal where id = #{setmealId}")
    Setmeal getById(Long setmealId);
}
