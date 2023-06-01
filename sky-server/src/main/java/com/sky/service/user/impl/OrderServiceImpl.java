package com.sky.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.user.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    WeChatPayUtil weChatPayUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO generateOrder(OrdersSubmitDTO dto) {
        //代码健壮性提升   若地址博id为空 无法生成订单
        if (dto.getAddressBookId() == null) {
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Orders order = new Orders();
        BeanUtils.copyProperties(dto, order);
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
        if (addressBook == null) {
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
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getByUserId(order.getUserId());
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
    /**
     * 历史订单查询
     * @param dto
     * @return
     */
    @Override
    public PageResult checkOrders(OrdersPageQueryDTO dto) {
        //设置当前用户id
        dto.setUserId(BaseContext.getCurrentId());
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<OrdersVO> page = orderMapper.getByDto(dto);
        page.forEach(ordersVO -> {
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(ordersVO.getId());
            ordersVO.setOrderDetailList(orderDetailList);
        });
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }
    @Override
    public OrdersVO checkByOrderId(Long id) {
        OrdersVO ordersVO = orderMapper.getByOrderId(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        ordersVO.setOrderDetailList(orderDetailList);
        return ordersVO;
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {
        orderMapper.updateOrderInfoByOrderId(id);
    }
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getInfoByOrderId(Long id) {
        //根据订单id查询订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        //将订单详情对象转换成购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream()
                .map(orderDetail -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    BeanUtils.copyProperties(orderDetail, shoppingCart);
                    shoppingCart.setUserId(BaseContext.getCurrentId());
                    return shoppingCart;
                }).collect(Collectors.toList());
        //将购物车信息批量插入到购物车中
        shoppingCartMapper.batchInsert(shoppingCartList);
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }
}
