package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper {

    List<Long> getSetmealIdsByDishIds(List<Long> ids);
    @AutoFill
    void insert(Setmeal setmeal);
}
