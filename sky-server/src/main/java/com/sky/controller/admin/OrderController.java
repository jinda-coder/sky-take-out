package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.OrderService;
import com.sky.vo.OrdersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单模块相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 订单搜索
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> orderSearch(OrdersPageQueryDTO dto){
        PageResult pageResult = orderService.orderSearch(dto);
        return Result.success(pageResult);
    }

    /**
     * 接单
     * @param orders
     * @return
     */
    @PutMapping("/confirm")
    public Result takeOrder(@RequestBody Orders orders){
        orderService.takeOrder(orders);
        return Result.success();
    }

    /**
     * 拒单
     * @param orders
     * @return
     */
    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody Orders orders){
        orderService.rejectionOrder(orders);
        return Result.success();
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrdersVO> checkOrderInfo(@PathVariable Long id){
        OrdersVO ordersVO = orderService.checkByOrderId(id);
        return Result.success(ordersVO);
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id){
        orderService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id){
        orderService.completeOrder(id);
        return Result.success();
    }
}
