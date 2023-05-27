package com.sky.service.admin.impl;

import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.admin.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl implements SetmealDishService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    public void insert(SetmealDish setmealDish) {
        setmealDishMapper.insert(setmealDish);
    }
}
