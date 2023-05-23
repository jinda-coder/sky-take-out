package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;

public interface SetmealService {
     void add(SetmealDTO dto);

    PageResult selectByPage(SetmealPageQueryDTO dto);

    Setmeal selectById(Long id);

    void updateInfo(SetmealDTO dto);
}
