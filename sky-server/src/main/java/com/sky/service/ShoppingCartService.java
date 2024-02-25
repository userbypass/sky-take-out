package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * @return void
     * @Description 添加购物车
     * @Date 2024/2/25 18:23
     * @Param [shoppingCartDTO]
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
    
    /**
     * @Description 获得购物车数据
     * @Date 2024/2/25 19:50
     * @Param []
     * @return java.util.List<com.sky.entity.ShoppingCart>
     */
    List<ShoppingCart> getShoppingCarts();

    /**
     * @Description 清空购物车
     * @Date 2024/2/25 19:57
     * @Param []
     * @return void
     */
    void cleanShoppingCarts();

    /**
     * @Description 删除购物车中某一商品
     * @Date 2024/2/25 20:04
     * @Param [shoppingCartDTO]
     * @return void
     */
    void subShoppingCarts(ShoppingCartDTO shoppingCartDTO);
}
