package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关接口")
public class DishController {
    @Autowired
    DishService dishService;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品，参数:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page( DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询，参数:{}",dishPageQueryDTO);
        return Result.success(dishService.page(dishPageQueryDTO));
    }
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品，参数:{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }
    @PostMapping("status/{status}")
    public Result updateStatus(@PathVariable Integer status,Long id){
        log.info("修改菜品起售状态，参数:{}",status);
        dishService.updateStatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> query(@PathVariable Long id){
        log.info("查询菜品信息,参数:{}",id);
        return Result.success(dishService.getByIdWithFlavor(id));
    }
}
