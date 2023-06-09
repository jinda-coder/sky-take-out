package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.admin.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult selectByPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Category> page = categoryMapper.selectByPage(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Category> list(Integer type) {
        List<Category> list = categoryMapper.list(type);
        return list;
    }
    /**
     * 添加分类
     * @param dto
     * @return
     */
    @Override
    public void add(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto,category);
        category.setStatus(StatusConstant.ENABLE);
        categoryMapper.add(category);
    }
    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    @Override
    public void updateInfo(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto,category);
        categoryMapper.updateInfo(category);
    }

    @Override
    public void disable(Integer status, Long id) {
        categoryMapper.disable(status,id);
    }
}
