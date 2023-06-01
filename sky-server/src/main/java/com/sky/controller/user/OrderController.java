package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.user.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "订单相关接口")
@Slf4j
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

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id){
        orderService.cancelOrder(id);
        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    public Result newOrder(@PathVariable Long id){
        orderService.getInfoByOrderId(id);
        return Result.success();
    }
    /**
     * 订单支付
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);
        OrderPaymentVO orderPaymentVO = OrderPaymentVO.builder()
                .paySign("adhjasjhda")
                .packageStr("aeqeqeqweqw")
                .signType("sadd1")
                .timeStamp("213121515")
                .nonceStr("asd12e11")
                .build();
        return Result.success(orderPaymentVO);
    }
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id){
        return Result.success();
    }
}
