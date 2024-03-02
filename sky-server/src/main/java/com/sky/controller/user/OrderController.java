package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * @return com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     * @Description 用户下单
     * @Date 2024/2/26 14:32
     * @Param [ordersSubmitDTO]
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> orderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，参数：{}", ordersSubmitDTO);
        return Result.success(orderService.orderSubmit(ordersSubmitDTO));
    }

    /**
     * @return com.sky.result.Result<com.sky.vo.OrderPaymentVO>
     * @Description 订单支付
     * @Date 2024/2/27 20:59
     * @Param [ordersPaymentDTO]
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 调用微信下单接口
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        // 返回支付参数
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @Description 查询历史记录
     * @Date 2024/2/27 21:16
     * @Param [ordersPageQueryDTO]
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> showHistoryRecords(Integer page, Integer pageSize, Integer status) {
        log.info("历史记录查询，页数：{},页大小：{},订单状态：{}", page, pageSize, status);
        return Result.success(orderService.getHistoryRecords(page, pageSize, status));
    }

    /**
     * @return com.sky.result.Result<com.sky.vo.OrderVO>
     * @Description 根据订单Id查询订单详情
     * @Date 2024/2/28 14:16
     * @Param [id]
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> showOrderDetail(@PathVariable Long id) {
        log.info("订单详情查询，参数：{}", id);
        return Result.success(orderService.getWithDetailByOrderId(id));
    }

    /**
     * @return com.sky.result.Result
     * @Description 取消订单
     * @Date 2024/2/28 14:43
     * @Param [id]
     */
    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id) {
        log.info("取消订单，参数：{}", id);
        orderService.cancelOrderByOrderId(id);
        return Result.success();
    }
    
    /**
     * @Description 重新插入购物车数据
     * @Date 2024/2/28 15:55
     * @Param [id]
     * @return com.sky.result.Result
     */
    @PostMapping("/repetition/{id}")
    public Result ReOrder(@PathVariable Long id) {
        log.info("再来一单，参数：{}", id);
        orderService.reOrderSubmit(id);
        return Result.success();
    }
    
    /**
     * @Description 用户催单
     * @Date 2024/3/1 18:15
     * @Param [id]
     * @return com.sky.result.Result
     */
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id){
        log.info("用户催单，参数{}",id);
        orderService.reminder(id);
        return Result.success();
    }

}
