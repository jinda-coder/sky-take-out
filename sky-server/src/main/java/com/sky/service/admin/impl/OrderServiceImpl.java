package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.admin.OrderService;
import com.sky.vo.OrdersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("adminOrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 订单搜索
     *
     * @return
     */
    @Override
    public PageResult orderSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        //查询所有订单信息  返回的是一个List集合
        Page<OrdersVO> page = orderMapper.selectAllOrders(dto);
        page.forEach(ordersVO -> {
            //根据订单id查询菜品信息
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(ordersVO.getId());
            StringBuilder sb = new StringBuilder();
            //拼接菜品信息
            orderDetailList.forEach(orderDetail -> {sb.append(orderDetail.getName() + "*" + orderDetail.getNumber() + ";\t");});
            //设置菜品信息
            ordersVO.setOrderDishes(sb.toString());
        });
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }
}

