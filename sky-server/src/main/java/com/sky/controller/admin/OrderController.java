package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        log.info("查询订单详情，参数：{}",id);
        return Result.success(orderService.queryOrderDetail(id));
    }
}
