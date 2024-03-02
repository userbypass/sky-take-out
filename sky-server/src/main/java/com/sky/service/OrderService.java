package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * @return com.sky.vo.OrderSubmitVO
     * @Description 用户下单
     * @Date 2024/2/26 14:35
     * @Param [ordersSubmitDTO]
     */
    OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * @return com.sky.result.PageResult
     * @Description 查询历史记录
     * @Date 2024/2/27 21:18
     * @Param [ordersPageQueryDTO]
     */
    PageResult getHistoryRecords(Integer page, Integer pageSize, Integer status);

    /**
     * @return com.sky.vo.OrderVO
     * @Description 根据订单Id查询订单详情
     * @Date 2024/2/28 14:17
     * @Param [orderId]
     */
    OrderVO getWithDetailByOrderId(Long orderId);

    /**
     * @return void
     * @Description 取消订单
     * @Date 2024/2/28 14:44
     * @Param [id]
     */
    void cancelOrderByOrderId(Long id);

    /**
     * @return void
     * @Description 重新插入购物车数据
     * @Date 2024/2/28 15:24
     * @Param [id]
     */
    void reOrderSubmit(Long id);

    /**
     * @return com.sky.result.PageResult
     * @Description 条件搜索订单信息
     * @Date 2024/2/28 23:26
     * @Param [ordersPageQueryDTO]
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @return com.sky.vo.OrderVO
     * @Description 查询订单详情
     * @Date 2024/2/29 10:38
     * @Param [id]
     */
    OrderVO queryOrderDetail(Long orderId);

    /**
     * @return com.sky.vo.OrderStatisticsVO
     * @Description 统计各个状态的订单数量
     * @Date 2024/2/29 11:02
     * @Param []
     */
    OrderStatisticsVO getStatistics();

    /**
     * @return void
     * @Description 接单
     * @Date 2024/2/29 12:31
     * @Param [ordersConfirmDTO]
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * @return void
     * @Description 拒单
     * @Date 2024/2/29 13:03
     * @Param [ordersRejectionDTO]
     */
    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * @return void
     * @Description 取消订单
     * @Date 2024/2/29 13:03
     * @Param [ordersCancelDTO]
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * @return void
     * @Description 派送订单
     * @Date 2024/2/29 13:54
     * @Param [orderId]
     */
    void deliveryOrder(Long orderId);
    
    /**
     * @Description 完成订单
     * @Date 2024/2/29 14:01
     * @Param [orderId]
     * @return void
     */
    void completeOrder(Long orderId);

    /**
     * @Description 用户催单
     * @Date 2024/3/1 18:16
     * @Param [id]
     * @return void
     */
    void reminder(Long id);
}
