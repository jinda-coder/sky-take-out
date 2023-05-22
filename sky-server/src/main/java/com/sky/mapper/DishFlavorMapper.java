package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.*;
import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insert(@Param("flavors") List<DishFlavor> flavor);

    List<DishFlavor> selectByDishId(Long id);

    void delete(Long id);
}
