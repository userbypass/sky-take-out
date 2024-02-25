package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {
    /**
     * @return void
     * @Description 添加购物车
     * @Date 2024/2/25 18:23
     * @Param [shoppingCartDTO]
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
