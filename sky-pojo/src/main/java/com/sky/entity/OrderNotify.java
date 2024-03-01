package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotify {
    // 新订单提示
    public static final Integer NEW_ORDER = 1;
    // 用户催单提示
    public static final Integer CUSTOMER_REMINDER = 2;
    Integer type;
    Long orderId;
    String content;
}
