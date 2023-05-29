package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    ShoppingCart getByShoppingCart(ShoppingCart shoppingCart);

    void insert(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart1);

    List<ShoppingCart> selectAll(Long userId);

    void deleteByUserId(Long userId);
}
