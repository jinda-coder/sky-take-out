package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    ShoppingCart getByShoppingCart(ShoppingCart shoppingCart);

    void insert(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart1);

    List<ShoppingCart> selectAll(Long userId);

    void deleteByUserId(Long userId);

    ShoppingCart getByUserIdAndSetmealId(@Param("userId") Long userId, @Param("setmealId") Long setmealId);

    void deleteByUserIdAndSetmealId(@Param("userId") Long userId, @Param("setmealId") Long setmealId);

    ShoppingCart getByUserIdAndDishId(@Param("userId") Long userId,@Param("dishId") Long dishId);

    void deleteByShoppingCart(ShoppingCart cart);

    List<ShoppingCart> getByUserId(Long UerId);

    void batchInsert(@Param("shoppingList") List<ShoppingCart> shoppingCartList);

}
