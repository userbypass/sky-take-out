package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * @return com.sky.result.Result
     * @Description 添加购物车
     * @Date 2024/2/25 18:21
     * @Param [shoppingCartDTO]
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车，参数：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * @return com.sky.result.Result<java.util.List < com.sky.entity.ShoppingCart>>
     * @Description 查询购物车信息
     * @Date 2024/2/25 19:47
     * @Param []
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("查询购物车信息");
        return Result.success(shoppingCartService.getShoppingCarts());
    }

    /**
     * @return com.sky.result.Result
     * @Description 清空购物车
     * @Date 2024/2/25 19:59
     * @Param []
     */
    @DeleteMapping("/clean")
    public Result clean() {
        log.info("清空购物车");
        shoppingCartService.cleanShoppingCarts();
        return Result.success();
    }
    
    /**
     * @Description 删除购物车中某一商品
     * @Date 2024/2/25 20:03
     * @Param [shoppingCartDTO]
     * @return com.sky.result.Result
     */
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.subShoppingCarts(shoppingCartDTO);
        return Result.success();
    }

}
