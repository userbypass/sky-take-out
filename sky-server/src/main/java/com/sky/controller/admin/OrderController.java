package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @Description 条件搜索订单信息
     * @Date 2024/2/29 10:36
     * @Param [ordersPageQueryDTO]
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("条件搜索订单信息，参数：{}", ordersPageQueryDTO);
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }

    /**
     * @return com.sky.result.Result<com.sky.vo.OrderVO>
     * @Description 查询订单详情
     * @Date 2024/2/29 10:37
     * @Param [id]
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> queryOrderDetail(@PathVariable Long id) {
        log.info("查询订单详情，参数：{}", id);
        return Result.success(orderService.queryOrderDetail(id));
    }

    /**
     * @return com.sky.result.Result<com.sky.vo.OrderStatisticsVO>
     * @Description 统计各个状态的订单数量
     * @Date 2024/2/29 10:58
     * @Param []
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> queryOrderDetail() {
        log.info("统计各个状态的订单数量");
        return Result.success(orderService.getStatistics());
    }

    /**
     * @return com.sky.result.Result
     * @Description 接单
     * @Date 2024/2/29 12:30
     * @Param [ordersConfirmDTO]
     */
    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单，参数{}", ordersConfirmDTO);
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * @return com.sky.result.Result
     * @Description 拒单
     * @Date 2024/2/29 12:36
     * @Param [ordersRejectionDTO]
     */
    @PutMapping("/rejection")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单，参数{}", ordersRejectionDTO);
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * @return com.sky.result.Result
     * @Description 取消订单
     * @Date 2024/2/29 13:03
     * @Param [ordersCancelDTO]
     */
    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单，参数{}", ordersCancelDTO);
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }
}
