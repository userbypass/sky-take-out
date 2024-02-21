package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理相关接口")
public class SetMealController {
    @Autowired
    SetMealService setMealService;
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO pageQueryDTO){
        return Result.success(setMealService.pageQuery(pageQueryDTO));
    }
    @GetMapping("{id}")
    @ApiOperation("根据ID查询")
    public Result<SetmealVO> query(@PathVariable Long id){
        return  Result.success(setMealService.getWithDish(id));
    }
}
