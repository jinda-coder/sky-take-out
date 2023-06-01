package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersVO extends Orders {
    String orderDishes;
    List<OrderDetail> orderDetailList;
}
