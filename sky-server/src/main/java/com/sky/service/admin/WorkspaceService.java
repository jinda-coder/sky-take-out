package com.sky.service.admin;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;

public interface WorkspaceService {
    BusinessDataVO businessData();

    OrderOverViewVO overviewOrders();

    DishOverViewVO getOverviewDishes();
}
