package com.sky.service.admin;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrdersVO;

public interface OrderService {
    PageResult orderSearch(OrdersPageQueryDTO dto);

    void takeOrder(Orders orders);

    void rejectionOrder(Orders orders);

    OrdersVO checkByOrderId(Long id);

    void deliveryOrder(Long id);

    void completeOrder(Long id);

    void cancelOrder(Orders orders);
}
