package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MyTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void handelTimeoutOrder() {
        //1.找出超时订单
        //1.设置状态
        Integer status = Orders.PENDING_PAYMENT;
        //设置15分钟之前的时间 如果下单时间 < 这个时间 说明该订单已超时
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> orders = orderMapper.getByStatusAndOrderTime(status,time);
        //更改超时订单的状态、取消原因
        orders.forEach(o->{
            o.setStatus(Orders.CANCELLED);
            o.setCancelReason(MessageConstant.ORDER_TIMEOUT);
            o.setCancelTime(LocalDateTime.now());
            orderMapper.update(o);
        });
    }
}

