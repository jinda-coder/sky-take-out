package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;

import java.awt.*;
import java.util.List;

@Mapper
public interface DishMapper {

    @Insert("insert into dish (name, category_id, price, " +
            "image, description, create_time, update_time, " +
            "create_user,update_user, status)\n" +
            "values (#{name}, #{categoryId}, #{price}, #{image}," +
            " #{description}, #{createTime}, #{updateTime}, #{createUser},\n" +
            "#{updateUser}, #{status})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    @AutoFill
    void insert(Dish dish);

    List<DishVO> selectAll(DishPageQueryDTO dto);

    void delete(List<Long> ids);

    Dish getById(Long id);
    void updateInfo(DishVO dishVO);

    Dish selectById(Long id);
    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(@Param("status") Integer status,@Param("id") Long id);
}
