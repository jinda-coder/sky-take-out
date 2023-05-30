package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrdersVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insert(Orders order);

    Page<OrdersVO> getByUserId(Long currentId);

    Page<OrdersVO> getByDto(OrdersPageQueryDTO dto);
}
