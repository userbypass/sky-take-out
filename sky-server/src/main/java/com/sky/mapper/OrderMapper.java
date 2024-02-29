package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * @return void
     * @Description 插入订单信息
     * @Date 2024/2/26 15:41
     * @Param [orders]
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * @return com.github.pagehelper.Page<com.sky.entity.Orders>
     * @Description 根据用户Id和订单状态查询订单信息
     * @Date 2024/2/27 21:49
     * @Param [userId]
     */

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @return com.sky.entity.Orders
     * @Description 根据订单id查询订单信息
     * @Date 2024/2/28 14:20
     * @Param [orderId]
     */
    @Select("select * from orders where id = #{orderId}")
    Orders getByOrderId(Long orderId);

    /**
     * @Description 统计各个状态的订单数量
     * @Date 2024/2/29 11:02
     * @Param []
     * @return com.sky.vo.OrderStatisticsVO
     */
    @Select("select count(status = #{confirmed} or null) as confirmed,count(status = #{deliveryInProgress} or null) as deliveryInProgress,count(status = #{toBeConfirmed} or null) as toBeConfirmed from orders")
    OrderStatisticsVO getOrderStatusStatistics(Map<String, Integer> orderStatus);
}
