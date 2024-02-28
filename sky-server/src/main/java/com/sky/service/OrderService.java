package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
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
     * @Description 重新插入购物车数据
     * @Date 2024/2/28 15:24
     * @Param [id]
     * @return void
     */
    void reOrderSubmit(Long id);
}
