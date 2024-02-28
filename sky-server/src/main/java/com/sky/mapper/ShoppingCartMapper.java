package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
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
     * @Description 根据购物车的ID修改购买商品的份数
     * @Date 2024/2/25 18:48
     * @Param [cart]
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumByCartId(ShoppingCart cart);

    /**
     * @return void
     * @Description 插入购物车
     * @Date 2024/2/25 19:01
     * @Param [shoppingCart]
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * @return void
     * @Description 根源用户id清空购物车
     * @Date 2024/2/25 19:59
     * @Param [userId]
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * @return void
     * @Description 根据购物车Id删除购物车
     * @Date 2024/2/25 20:20
     * @Param [id]
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteByCartId(Long id);

    /**
     * @return void
     * @Description 批量插入购物车
     * @Date 2024/2/28 15:49
     * @Param [shoppingCartList]
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
