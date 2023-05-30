package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.user.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.vo.OrdersVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 订单提交
     * @param dto
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO dto){
        return Result.success(orderService.generateOrder(dto));
    }

    /**
     * 历史订单查询
     * @param dto
     * @return
     */
    @GetMapping("historyOrders")
    public Result<PageResult> checkOrders(OrdersPageQueryDTO dto){
        PageResult pageResult = orderService.checkOrders(dto);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrdersVO> checkOrderInfo(@PathVariable Long id){
        OrdersVO ordersVO = orderService.checkByOrderId(id);
        return Result.success(ordersVO);
    }
}
