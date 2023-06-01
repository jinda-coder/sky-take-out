package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.OrderService;
import com.sky.vo.OrdersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

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
    /**
     * 接单
     * @param orders
     * @return
     */
    @Override
    public void takeOrder(Orders orders) {
        orderMapper.takeOrder(orders);
    }

    /**
     * 拒单
     * @param orders
     */
    @Override
    public void rejectionOrder(Orders orders) {
        orderMapper.rejectionOrder(orders);
    }
    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrdersVO checkByOrderId(Long id) {
        OrdersVO ordersVO = orderMapper.getByOrderId(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        ordersVO.setOrderDetailList(orderDetailList);
        return ordersVO;
    }
    /**
     * 派送订单
     * @param id
     * @return
     */
    @Override
    public void deliveryOrder(Long id) {
        orderMapper.deliveryOrder(id);
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @Override
    public void completeOrder(Long id) {
        orderMapper.completeOrder(id);
    }
}

