package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理相关接口")
public class SetMealController {
    @Autowired
    SetMealService setMealService;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO pageQueryDTO) {
        return Result.success(setMealService.pageQuery(pageQueryDTO));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询")
    public Result<SetmealVO> query(@PathVariable Long id) {
        return Result.success(setMealService.getWithDish(id));
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setMealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改套餐起售状态")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result updateStatus(@PathVariable Integer status,Long id){
        setMealService.updateStatus(status,id);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result addWithDishes(@RequestBody SetmealDTO setmealDTO){
        setMealService.addWithDishes(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result deleteBatch(@RequestParam List<Long> ids){
        setMealService.deleteBatchWithDishes(ids);
        return Result.success();
    }
}
