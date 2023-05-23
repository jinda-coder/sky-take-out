package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface SetmealMapper {

    List<Long> getSetmealIdsByDishIds(List<Long> ids);
    @AutoFill
    void insert(Setmeal setmeal);

    Page<SetmealVO> selectByPage(SetmealPageQueryDTO dto);

    SetmealVO selectById(Long id);
    @AutoFill(OperationType.UPDATE)
    void updateInfo(Setmeal setmeal);
}
