package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * @Description 批量插入订单信息表
     * @Date 2024/2/26 15:46
     * @Param [orderDetails]
     * @return void
     */
    void insertBatch(List<OrderDetail> orderDetails);
}
