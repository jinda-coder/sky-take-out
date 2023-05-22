package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void save(DishDTO dto);

    PageResult selectByPage(DishPageQueryDTO dto);
    void delete(List<Long> ids);

    void updateInfo(DishDTO dto);

    DishVO selectById(Long id);

    void updateStatus(Integer status,Long id);
}
