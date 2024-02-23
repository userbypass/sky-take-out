package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * @return com.sky.result.Result
     * @Description 设置店铺营业状态
     * @Date 2024/2/23 15:00
     * @Param [status]
     */
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态:{}", status == 1 ? "营业中" : "打烊了");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * @return com.sky.result.Result<java.lang.Integer>
     * @Description 查询店铺营业状态
     * @Date 2024/2/23 15:00
     * @Param []
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("查询店铺营业状态:{}", status == 1 ? "营业中" : "打烊了");
        return Result.success(status);
    }
}
