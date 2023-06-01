package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.admin.OrderService;
import com.sky.vo.OrdersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminOrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    /**
     * 订单搜索
     * @return
     */
    @Override
    public PageResult orderSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<OrdersVO> page = orderMapper.selectAllOrders(dto);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }
}

