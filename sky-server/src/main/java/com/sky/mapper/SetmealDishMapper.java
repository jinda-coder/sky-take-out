package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    void insert(SetmealDish setmealDish);

    List<SetmealDish> selectBySetmealId(Long id);
}
