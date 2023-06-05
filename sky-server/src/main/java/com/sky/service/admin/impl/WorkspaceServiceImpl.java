package com.sky.service.admin.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.mapper.WorkspaceMapper;
import com.sky.service.admin.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Override
    public BusinessDataVO businessData() {
        //1.营业额
        Double turnover = workspaceMapper.getTurnover();
        //2.有效订单数
        Integer validOrderCount = workspaceMapper.getValidOrderCount();
        //3.订单完成率
        //3.1总订单数
        Integer totalCount = workspaceMapper.getTotalCount();
        //订单完成率
        Double orderCompletionRate = (double)validOrderCount/totalCount;
        //4.平均客单价
        //查询下单人数
        Integer userCount = workspaceMapper.getUserCount();
        Double unitPrice = turnover/userCount;
        //5.新增用户数
        Integer newUsers = workspaceMapper.getNewUsers();
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers).build();
    }

    @Override
    public OrderOverViewVO overviewOrders() {
        //1.全部订单
        Integer allOrders = workspaceMapper.getTotalCount();
        //2.已完成订单数
        Integer completedOrders = workspaceMapper.getOrderCountByStatus(Orders.COMPLETED);
        //3.已取消订单数
        Integer cancelledOrders = workspaceMapper.getOrderCountByStatus(Orders.CANCELLED);
        //4.待派送订单数
        Integer deliveredOrders = workspaceMapper.getOrderCountByStatus(Orders.CONFIRMED);
        //5.待接单数量
        Integer waitingOrders = workspaceMapper.getOrderCountByStatus(Orders.TO_BE_CONFIRMED);
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        Integer sold = workspaceMapper.getDishesByStatus(StatusConstant.ENABLE);
        Integer discontinued = workspaceMapper.getDishesByStatus(StatusConstant.DISABLE);
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
