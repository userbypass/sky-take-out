package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * @return java.util.List<com.sky.entity.ShoppingCart>
     * @Description 获取购物信息列表
     * @Date 2024/2/25 18:38
     * @Param [shoppingCart]
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * @return void
     * @Description 根据购物信息的ID修改购买商品的份数
     * @Date 2024/2/25 18:48
     * @Param [cart]
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumByCartId(ShoppingCart cart);

    /**
     * @return void
     * @Description 插入套餐信息
     * @Date 2024/2/25 19:01
     * @Param [shoppingCart]
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);
}
