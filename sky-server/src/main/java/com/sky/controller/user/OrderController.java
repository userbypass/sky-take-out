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
     * @Description 用户下单
     * @Date 2024/2/26 14:32
     * @Param [ordersSubmitDTO]
     * @return com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> orderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单，参数：{}",ordersSubmitDTO);
        return Result.success(orderService.orderSubmit(ordersSubmitDTO));
    }

    /**
     * @Description 订单支付
     * @Date 2024/2/27 20:59
     * @Param [ordersPaymentDTO]
     * @return com.sky.result.Result<com.sky.vo.OrderPaymentVO>
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
     * @Description 查询历史记录
     * @Date 2024/2/27 21:16
     * @Param [ordersPageQueryDTO]
     * @return com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> showHistoryRecords(Integer page,Integer pageSize,Integer status){
        log.info("历史记录查询，页数：{},页大小：{},订单状态：{}",page,pageSize,status);
        return Result.success(orderService.getHistoryRecords(page,pageSize,status));
    }
    /**
     * @Description 根据订单Id查询订单详情
     * @Date 2024/2/28 14:16
     * @Param [id]
     * @return com.sky.result.Result<com.sky.vo.OrderVO>
     */
    @GetMapping("orderDetail/{id}")
    public Result<OrderVO> showOrderDetail(@PathVariable Long id){
        log.info("订单详情查询，参数：{}",id);
        return Result.success(orderService.getWithDetailByOrderId(id));
    }

}
