package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetMealMapper setMealMapper;

    /**
     * @return void
     * @Description 添加购物车
     * @Date 2024/2/25 18:24
     * @Param [shoppingCartDTO]
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入到购物车的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        long userID = BaseContext.getCurrentId();
        shoppingCart.setUserId(userID);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        // 若存在，则数量加一
        if (shoppingCarts != null && !shoppingCarts.isEmpty()) {
            ShoppingCart cart = shoppingCarts.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumByCartId(cart);
        }
        // 若不存在，则插入购物车信息
        else {
            // 判断购买的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }
            // 是套餐
            else {
                Setmeal setmeal = setMealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * @return java.util.List<com.sky.entity.ShoppingCart>
     * @Description 获得购物车数据
     * @Date 2024/2/25 19:50
     * @Param []
     */
    @Override
    public List<ShoppingCart> getShoppingCarts() {
        return shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
    }

    /**
     * @return void
     * @Description 清空购物车
     * @Date 2024/2/25 19:57
     * @Param []
     */
    @Override
    public void cleanShoppingCarts() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    /**
     * @return void
     * @Description 删除购物车中某一商品
     * @Date 2024/2/25 20:04
     * @Param [shoppingCartDTO]
     */
    @Override
    public void subShoppingCarts(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        if (shoppingCarts != null && !shoppingCarts.isEmpty()) {
            ShoppingCart cart = shoppingCarts.get(0);
            // 判断删除商品的份数
            Integer currentNum = cart.getNumber();
            if (Objects.equals(1, currentNum)) {
                // 若当前份数为一，直接删除
                shoppingCartMapper.deleteByCartId(cart.getId());
            } else {
                // 若当前份数大于一，数量减一
                cart.setNumber(currentNum - 1);
                shoppingCartMapper.updateNumByCartId(cart);
            }
        }
    }
}
