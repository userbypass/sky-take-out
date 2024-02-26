package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    /**
     * @return void
     * @Description 插入订单信息
     * @Date 2024/2/26 15:41
     * @Param [orders]
     */
    void insert(Orders orders);
}
