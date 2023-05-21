package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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
}
