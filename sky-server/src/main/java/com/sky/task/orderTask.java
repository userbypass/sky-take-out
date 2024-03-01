package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@Slf4j
public class orderTask {
    @Autowired
    OrderMapper orderMapper;

    /**
     * @return void
     * @Description 处理超时未付款订单业务(每分钟检查一次)
     * @Date 2024/2/29 23:51
     * @Param []
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void processTimeoutOrders() {
        log.info("处理超时未付款订单 {}", LocalDateTime.now());
        // 订单付款时间设置为下单后15分钟内
        // 若当前时间-15分钟 > 下单时间，说明订单付款超时
        long timeLimit = 15L;
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(-timeLimit);
        //
        List<Orders> ordersList = orderMapper.selectExpiredOrder(Orders.PENDING_PAYMENT, expiredTime);
        if (ordersList != null && !ordersList.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("超时未付款");
                orders.setCancelTime(now);
                orderMapper.update(orders);
            });
        }
    }

    /**
     * @return void
     * @Description 处理派送中的订单(每天凌晨1点)
     * @Date 2024/3/1 0:18
     * @Param []
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrders() {
        log.info("处理派送中的订单 {}", LocalDateTime.now());
        // 当日上午1时0分0秒
        LocalDateTime expiredTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN.plusHours(1L));
        //
        List<Orders> ordersList = orderMapper.selectExpiredOrder(Orders.DELIVERY_IN_PROGRESS,expiredTime);
        if (ordersList != null && !ordersList.isEmpty()) {
            long timeLimit = 10L;
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);
                // 设置送达时间为下单时间 + 10分钟
                orders.setDeliveryTime(orders.getOrderTime().plusMinutes(timeLimit));
                orderMapper.update(orders);
            });
        }
    }
}
