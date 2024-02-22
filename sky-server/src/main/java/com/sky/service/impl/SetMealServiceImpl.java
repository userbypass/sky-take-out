package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
     * @return com.sky.vo.SetmealVO
     * @Description 根据ID查询套餐及包含菜品信息
     * @Date 2024/2/21 11:29
     * @Param [id]
     */
    @Override
    public SetmealVO getWithDish(Long setmealId) {
        Setmeal setmeal = setmealMapper.getById(setmealId);
        List<SetmealDish> setmealDishList = setMealDishMapper.getBySetmealId(setmealId);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * @return void
     * @Description 更新套餐及其包含菜品信息
     * @Date 2024/2/21 11:59
     * @Param [setmealDTO]
     */
    @Transactional
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新套餐信息
        setmealMapper.update(setmeal);
        // 删除原套餐-菜品对应关系
        setMealDishMapper.deleteBySetmealId(setmealDTO.getId());
        // 插入新套餐-菜品对应关系
        setMealDishMapper.insertBatch(setmealDTO.getSetmealDishes(), setmeal.getId());
    }

    /**
     * @return void
     * @Description 修改套餐起售状态
     * @Date 2024/2/22 12:26
     * @Param [status, id]
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 起售套餐不能包含未起售菜品
        if (Objects.equals(1, status)) {
            Integer haltSellDishCount = setMealDishMapper.getHaltSellDishCountBySetmealId(id);
            if (haltSellDishCount > 0) throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
        setmealMapper.updateStatus(Setmeal.builder().status(status).id(id).build());
    }

    /**
     * @return void
     * @Description 插入套餐信息
     * @Date 2024/2/22 15:14
     * @Param [setmealDTO]
     */
    @Override
    @Transactional
    public void addWithDishes(SetmealDTO setmealDTO) {
        // 插入套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        // 插入套餐-菜品表
        setMealDishMapper.insertBatch(setmealDTO.getSetmealDishes(), setmeal.getId());
    }

    /**
     * @return void
     * @Description 批量删除套餐及涉及的套餐-菜品信息
     * @Date 2024/2/22 15:49
     * @Param [ids]
     */
    @Override
    @Transactional
    public void deleteBatchWithDishes(List<Long> ids) {
        // 在售套餐不可删除
        Integer count = setmealMapper.getCountByIds(ids);
        if (count > 0) throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        // 批量删除套餐信息
        setmealMapper.deleteBatch(ids);
        // 批量删除套餐-菜品信息
        setMealDishMapper.deleteBatchBySetmealId(ids);
    }
}
