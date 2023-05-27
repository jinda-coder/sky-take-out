package com.sky.service.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
     void add(SetmealDTO dto);

    PageResult selectByPage(SetmealPageQueryDTO dto);

    SetmealVO selectById(Long id);

    void updateInfo(SetmealDTO dto);

    void deleteByIds(Integer[] ids);

    void disableStatus(Integer status,Integer id);

    List<Setmeal> list(Setmeal setmeal);
}
