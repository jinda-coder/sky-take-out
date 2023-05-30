package com.sky.service.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersVO;

public interface OrderService {
    OrderSubmitVO generateOrder(OrdersSubmitDTO dto);

    PageResult checkOrders(OrdersPageQueryDTO dto);

    OrdersVO checkByOrderId(Long id);
}
