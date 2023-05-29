package com.sky.service.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void add2ShoppingCart(ShoppingCartDTO dto);

    List<ShoppingCart> list();

    void clean();

    void sub(ShoppingCartDTO dto);
}
