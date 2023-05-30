package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.user.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/order/submit")
@Api(tags = "订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 订单提交
     * @param dto
     * @return
     */
    @PostMapping("")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO dto){
        return Result.success(orderService.generateOrder(dto));
    }
}
