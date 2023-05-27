package com.sky.service.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import java.util.List;

public interface CategoryService {

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> list(Integer type);

    void add(CategoryDTO dto);

    void delete(Integer id);

    void updateInfo(CategoryDTO dto);

    void disable(Integer status, Long id);

}
