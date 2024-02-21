package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    SetMealMapper setmealMapper;
    @Autowired
    SetMealDishMapper setMealDishMapper;

    /**
     * @return com.sky.result.PageResult
     * @Description 分页查询
     * @Date 2024/2/21 10:52
     * @Param [pageQueryDTO]
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<SetmealVO> pageResult = setmealMapper.pageQuery(pageQueryDTO);
        return new PageResult(pageResult.getTotal(), pageResult.getResult());
    }
    /**
     * @Description 根据ID查询套餐信息及包含菜品
     * @Date 2024/2/21 11:29
     * @Param [id]
     * @return com.sky.vo.SetmealVO
     */
    @Override
    public SetmealVO getWithDish(Long setmealId) {
        Setmeal setmeal = setmealMapper.getById(setmealId);
        List<SetmealDish> setmealDishList = setMealDishMapper.getBySetmealId(setmealId);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }
}
