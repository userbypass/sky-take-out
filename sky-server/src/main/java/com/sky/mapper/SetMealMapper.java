package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     * @return com.sky.entity.Setmeal
     * @Description 根据套餐ID查询套餐信息
     * @Date 2024/2/21 11:38
     * @Param [setmealId]
     */
    @Select("select * from setmeal where id = #{setmealId}")
    Setmeal getById(Long setmealId);

    /**
     * @return void
     * @Description 根据套餐ID更新套餐信息
     * @Date 2024/2/21 12:03
     * @Param [setmealDTO]
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * @return void
     * @Description 修改套餐起售状态
     * @Date 2024/2/22 12:30
     * @Param [status, id]
     */
    @AutoFill(OperationType.UPDATE)
    @Update("update setmeal set status = #{status}, update_time = #{updateTime}, update_user=#{updateUser} where id = #{id}")
    void updateStatus(Setmeal setmeal);
}
