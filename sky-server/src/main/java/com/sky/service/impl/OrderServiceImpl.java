package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    WeChatPayUtil weChatPayUtil;

    /**
     * @return com.sky.vo.OrderSubmitVO
     * @Description 用户下单
     * @Date 2024/2/26 14:35
     * @Param [ordersSubmitDTO]
     */
    @Override
    @Transactional
    public OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO) {
        // 地址为空不能下单
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (address == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 购物车为空不能下单
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(ShoppingCart.builder()
                .userId(userId).build());
        if (shoppingCarts == null || shoppingCarts.isEmpty())
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        // 向订单表插入一条数据
        Orders orders = Orders.builder()
                .number(String.valueOf(System.currentTimeMillis()))
                .status(Orders.PENDING_PAYMENT)
                .userId(userId)
                .orderTime(LocalDateTime.now())
                .payStatus(Orders.UN_PAID)
                .phone(address.getPhone())
                .consignee(address.getConsignee())
                .address(address.getDetail())
                .build();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orderMapper.insert(orders);
        // 向订单明细表插入n条数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        shoppingCarts.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetails);
        // 清空购物车
        shoppingCartMapper.deleteByUserId(userId);
        // 封装并返回 OrderSubmitVO
        return OrderSubmitVO.builder().id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        /*
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);
        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nonceStr", "");
        jsonObject.put("paySign", "");
        jsonObject.put("timeStamp", System.currentTimeMillis());
        jsonObject.put("signType", "RSA");
        jsonObject.put("packageStr", "");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return vo;
    }


    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public PageResult getHistoryRecords(Integer page, Integer pageSize, Integer status) {
        // 封装ordersPageQueryDTO作为分页查询对象
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setStatus(status);
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        // 分页查询
        PageHelper.startPage(page, pageSize);
        Page<Orders> ordersPage = orderMapper.pageQuery(ordersPageQueryDTO);
        List<Orders> ordersList = ordersPage.getResult();
        // 封装orderVOList作为查询结果
        List<OrderVO> orderVOList = new ArrayList<>();
        ordersList.forEach(orders -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(orders.getId()));
            BeanUtils.copyProperties(orders, orderVO);
            orderVOList.add(orderVO);
        });
        // 返回查询结果
        return new PageResult(ordersPage.getTotal(), orderVOList);
    }

    /**
     * @return com.sky.result.Result<com.sky.vo.OrderVO>
     * @Description 根据订单Id查询订单详情
     * @Date 2024/2/28 14:16
     * @Param [id]
     */
    @Override
    public OrderVO getWithDetailByOrderId(Long orderId) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderMapper.getByOrderId(orderId), orderVO);
        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(orderId));
        return orderVO;
    }

    @Override
    public void cancelOrderByOrderId(Long id) {
        Orders ordersOriginal = orderMapper.getByOrderId(id);
        // 判断订单是否存在
        if (ordersOriginal == null) throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 判断订单状态
        Integer status = ordersOriginal.getStatus();
        // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
        // 支付状态 0未支付 1已支付 2退款
        if (status > Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersOriginal, orders);
        // 设置订单状态为已取消
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        // 待付款状态直接取消
        // 已付款待接单状态取消设置支付状态为退款
        if (status.equals(Orders.TO_BE_CONFIRMED)) {
            orders.setPayStatus(Orders.REFUND);
        }
        orderMapper.update(orders);
    }

    /**
     * @return void
     * @Description 重新插入购物车数据
     * @Date 2024/2/28 15:40
     * @Param [id]
     */
    @Override
    public void reOrderSubmit(Long id) {
        // 取出旧订单信息
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        // 构造新购物车信息
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();
        orderDetailList.forEach(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(now);
            shoppingCartList.add(shoppingCart);
        });
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * @return com.sky.result.PageResult
     * @Description 条件查询订单信息
     * @Date 2024/2/28 23:28
     * @Param [ordersPageQueryDTO]
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 检索订单信息
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> ordersPage = orderMapper.pageQuery(ordersPageQueryDTO);
        // 检索结果封装为 List<OrderVO>对象
        List<OrderVO> orderVOList = getOrderVOList(ordersPage);
        return new PageResult(ordersPage.getTotal(), orderVOList);
    }

    /**
     * @return com.sky.vo.OrderVO
     * @Description 查询订单详情
     * @Date 2024/2/29 10:38
     * @Param [id]
     */
    @Override
    public OrderVO queryOrderDetail(Long orderId) {
        Orders orders = orderMapper.getByOrderId(orderId);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailMapper.getByOrderId(orderId));
        orderVO.setOrderDishes(getOrderDishes(orders));
        return orderVO;
    }

    /**
     * @return com.sky.vo.OrderStatisticsVO
     * @Description 统计各个状态的订单数量
     * @Date 2024/2/29 11:02
     * @Param []
     */
    @Override
    public OrderStatisticsVO getStatistics() {
        Map<String, Integer> orderStatus = new HashMap<>();
        orderStatus.put("confirmed", Orders.CONFIRMED);
        orderStatus.put("deliveryInProgress", Orders.DELIVERY_IN_PROGRESS);
        orderStatus.put("toBeConfirmed", Orders.TO_BE_CONFIRMED);
        return orderMapper.getOrderStatusStatistics(orderStatus);
    }

    /**
     * @return void
     * @Description 接单
     * @Date 2024/2/29 12:31
     * @Param [ordersConfirmDTO]
     */
    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        orderMapper.update(Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build());
    }

    /**
     * @return void
     * @Description 拒单
     * @Date 2024/2/29 12:36
     * @Param [ordersRejectionDTO]
     */
    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Long orderId = ordersRejectionDTO.getId();
        Orders ordersOriginal = orderMapper.getByOrderId(orderId);
        // 订单不存在无法拒单
        if (ordersOriginal == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 订单处于待接单状态才能拒单
        if (Objects.equals(ordersOriginal.getStatus(), Orders.TO_BE_CONFIRMED)) {
            Orders orders = Orders.builder()
                    .id(orderId)
                    .status(Orders.CANCELLED)
                    .cancelTime(LocalDateTime.now())
                    .cancelReason(ordersRejectionDTO.getRejectionReason())
                    .build();
            // 如果用户在拒单时已付款，还需要执行退款操作
            if (Objects.equals(ordersOriginal.getPayStatus(), Orders.PAID)) {
                System.out.println("执行退款操作...");
                orders.setPayStatus(Orders.REFUND);
            }
            orderMapper.update(orders);
        }
        else throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
    }

    /**
     * @return java.util.List<com.sky.vo.OrderVO>
     * @Description 将订单的菜品信息封装到orderVO中，并添加到orderVOList
     * @Date 2024/2/29 0:40
     * @Param [ordersPage]
     */
    private List<OrderVO> getOrderVOList(Page<Orders> ordersPage) {
        // 订单列表
        List<Orders> ordersList = ordersPage.getResult();
        // 封装的返回对象
        List<OrderVO> orderVOList = new ArrayList<>();
        ordersList.forEach(orders -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            // 设置菜品信息字符串
            orderVO.setOrderDishes(getOrderDishes(orders));
            orderVOList.add(orderVO);
        });
        return orderVOList;
    }

    /**
     * @return java.lang.String
     * @Description 根据订单id获取菜品信息字符串
     * @Date 2024/2/29 0:47
     * @Param [orders]
     */
    private String getOrderDishes(Orders orders) {
        // 根据订单id获得订单详情列表
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        // 拼接 orderDishes字段 规则-> 菜品名称1*份数1; 菜品名称2*份数2;
        List<String> orderDishes = new ArrayList<>();
        orderDetailList.forEach(orderDetail -> {
            orderDishes.add(orderDetail.getName() + '*' + orderDetail.getNumber() + ';');
        });
        return String.join(" ", orderDishes);
    }
}
