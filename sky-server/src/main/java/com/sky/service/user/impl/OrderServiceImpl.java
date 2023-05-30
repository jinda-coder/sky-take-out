package com.sky.service.user.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.user.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO generateOrder(OrdersSubmitDTO dto) {
        //代码健壮性提升   若地址博id为空 无法生成订单
        if(dto.getAddressBookId() == null){
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Orders order = new Orders();
        BeanUtils.copyProperties(dto,order);
        //设置订单号
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        //设置订单状态
        order.setStatus(Orders.PENDING_PAYMENT);
        //设置下单用户id
        order.setUserId(BaseContext.getCurrentId());
        //设置下单时间
        order.setOrderTime(LocalDateTime.now());
        //设置支付状态
        order.setPayStatus(Orders.UN_PAID);
        //查询地址簿补充地址信息
        AddressBook addressBook = addressBookMapper.getById(order.getAddressBookId());
        if (addressBook == null){
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //设置收货人
        order.setConsignee(addressBook.getConsignee());
        //设置地址
        order.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        //设置手机号
        order.setPhone(addressBook.getPhone());
        //将订单信息插入数据库
        orderMapper.insert(order);
        //生成订单详情
        //根据用户id查询购物车信息
        List<ShoppingCart> shoppingCartList =  shoppingCartMapper.getByUserId(order.getUserId());
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(order.getId());
            return orderDetail;
        }).collect(Collectors.toList());
        //讲订单信息批量插入订单详情表中
        orderDetailMapper.insertBatch(orderDetailList);
        //删除该用户购物车信息
        shoppingCartMapper.deleteByUserId(order.getUserId());
        //封装VO对象  并返回
        return OrderSubmitVO.builder()
                .orderTime(order.getOrderTime())
                .id(order.getId())
                .orderAmount(order.getAmount())
                .orderNumber(order.getNumber())
                .build();
    }
}
