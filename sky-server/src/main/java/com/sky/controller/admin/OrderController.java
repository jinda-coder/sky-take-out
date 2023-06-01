package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.OrderService;
import com.sky.vo.OrdersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
