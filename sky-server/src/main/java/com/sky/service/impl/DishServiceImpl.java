package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 添加菜品
     * @param dto
     */
    @Override
    //由于有两条sql语句，所以添加事务管理
    @Transactional(rollbackFor = Exception.class)
    public void save(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto,dish);
        dishMapper.insert(dish);
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null && flavors.size() != 0){
            flavors.forEach(f -> {f.setDishId(dish.getId());});
        }
        dishFlavorMapper.insert(flavors);
    }
    /**
     * 菜品分类分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult selectByPage(DishPageQueryDTO dto) {
        DishVO dishVO = new DishVO();
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<DishVO> dishVOS = (Page<DishVO>)dishMapper.selectAll();
        BeanUtils.copyProperties(dto,dishVO);
        PageResult build = PageResult.builder()
                .total(dishVOS.getTotal())
                .records(dishVOS.getResult())
                .build();
        return build;
    }
}
