package com.sky.service.user.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.user.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 购物车添加商品
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add2ShoppingCart(ShoppingCartDTO dto) {
        ShoppingCart shoppingCart = new ShoppingCart();
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //设置用户id
        shoppingCart.setUserId(userId);
        //设置口味
        shoppingCart.setDishFlavor(dto.getDishFlavor());
        //设置当前时间
        shoppingCart.setCreateTime(LocalDateTime.now());
        //1.判断添加的是套餐还是菜品
        if (dto.getSetmealId() != null){
            //是套餐 查询套餐表  补充购物车表中相关字段的信息
            SetmealVO setmealVO = setmealMapper.selectById(dto.getSetmealId());
            shoppingCart.setName(setmealVO.getName());
            shoppingCart.setImage(setmealVO.getImage());
            shoppingCart.setAmount(setmealVO.getPrice());
            shoppingCart.setSetmealId(setmealVO.getId());
        }else if (dto.getDishId() != null){
            //是菜品  查询数据库  补充购物车表中相关字段信息
            Dish dish = dishMapper.selectById(dto.getDishId());
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setDishId(dish.getId());
            shoppingCart.setAmount(dish.getPrice());
        }else{
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_ID_IS_NULL);
        }
        //查询购物车中是否已经存在当前添加的项
        ShoppingCart shoppingCart1 = shoppingCartMapper.getByShoppingCart(shoppingCart);
        if (shoppingCart1 == null){
            //如果不存在，将数量设为1，insert到表中
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
        }else {
            //如果存在，将数量+1  update回表
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart1);
        }
    }

    /**
     * 查看购物车商品
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.selectAll(userId);
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
