package com.sky.service.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {
    OrderSubmitVO generateOrder(OrdersSubmitDTO dto);
}
