package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> list(Integer type);
    @AutoFill
    @Insert("insert into category (type,name,sort,status,create_time,update_time,create_user,update_user) values " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Category category);
    @Delete("delete from category where id = #{id}")
    void delete(Integer id);
    @Update("update category set type = #{type},name = #{name}," +
            "sort = #{sort},status = #{status}," +
            "update_time = #{updateTime},update_user = #{updateUser} where id = #{id}")
    @AutoFill(OperationType.UPDATE)
    void updateInfo(Category category);
    @Update("update category set status = #{status} where id = #{id}")
    void disable(@Param("status") Integer status,@Param("id") Long id);
}
