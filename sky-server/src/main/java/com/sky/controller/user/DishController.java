package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 查询Redis中是否存在请求数据
        String key = "dish_" + categoryId;
        Object value = redisTemplate.opsForValue().get(key);
        //Todo: 需要某种机制确保强转转换不会抛出异常
        List<DishVO> dishVOList = (List<DishVO>) value;
        // 若列表存在且非空，则返回请求数据
        if (dishVOList != null && !dishVOList.isEmpty()) {
            return Result.success(dishVOList);
        }
        // 若不存在，则查找数据库，并存储到Redis中
        Dish dish = new Dish();
        // 查询指定种类且状态为起售中的菜品
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        dishVOList = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, dishVOList);
        return Result.success(dishVOList);
    }

}
