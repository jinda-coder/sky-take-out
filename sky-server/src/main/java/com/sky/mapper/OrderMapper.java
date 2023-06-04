package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.OrderStatus;
import com.sky.entity.Orders;
import com.sky.vo.OrdersVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void insert(Orders order);

    Page<OrdersVO> getByUserId(Long currentId);

    Page<OrdersVO> getByDto(OrdersPageQueryDTO dto);

    OrdersVO getByOrderId(Long id);

    void updateOrderInfoByOrderId(Long id);
    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<OrdersVO> selectAllOrders(OrdersPageQueryDTO dto);

    void takeOrder(Orders orders);

    void rejectionOrder(Orders orders);

    void deliveryOrder(Long id);

    void completeOrder(Long id);

    void cancelOrder(Orders orders);

    List<OrderStatus> statistics();

    void updateStatus(OrdersPaymentDTO ordersPaymentDTO);
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTime(@Param("status") Integer status, @Param("time") LocalDateTime time);
}

