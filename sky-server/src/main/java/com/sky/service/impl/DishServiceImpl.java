package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetMealDishMapper setMealDishMapper;

    /**
     * @return void
     * @Description 新增菜品及相应口味
     * @Date 2024/2/19 10:49
     * @Param [dishDTO]
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //插入1条菜品表数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        //插入多条口味表数据
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            dishFlavorMapper.insert(flavors);
        }
    }

    /**
     * @return com.sky.result.PageResult
     * @Description 分页查询
     * @Date 2024/2/20 14:19
     * @Param [dishPageQueryDTO]
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),
                dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishFlavorMapper.dishPageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @return void
     * @Description 批量删除
     * @Date 2024/2/20 14:19
     * @Param [ids]
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否起售
        ids.forEach(id -> {
            Dish dish = dishMapper.selectById(id);
            if (dish == null) throw new DeletionNotAllowedException(MessageConstant.DISH_NOT_EXIST);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        });
        // 判断菜品是否关联套餐
        List<Long> setMealIdList = setMealDishMapper.selectByDishIds(ids);
        if (setMealIdList != null && !setMealIdList.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品
        dishMapper.deleteByIds(ids);
        // 删除口味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * @return void
     * @Description 修改菜品起售状态
     * @Date 2024/2/20 15:51
     * @Param [status]
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        dishMapper.updateStatus(status, id);
    }

    /**
     * @return com.sky.vo.DishVO
     * @Description 根据菜品ID查询菜品及其口味信息
     * @Date 2024/2/20 18:12
     * @Param [id]
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.selectById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * @return void
     * @Description 修改菜品及其口味
     * @Date 2024/2/20 19:02
     * @Param [dishDTO]
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        //更新1条菜品表数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateById(dish);
        //删除旧菜品表数据
        Long dishId = dish.getId();
        List<Long> dishIdList = new ArrayList<>();
        dishIdList.add(dishId);
        dishFlavorMapper.deleteByDishIds(dishIdList);
        //插入多条口味表数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insert(flavors);
        }
    }

    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<Dish> getByDishName(String dishName) {
        return dishMapper.selectByDishName(dishName);
    }
}
