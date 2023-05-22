package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 菜品删除功能
     * @param ids
     * @return
     */
    public void delete(List<Long> ids){
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){
                //当前菜品处于起售状态  无法删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        dishMapper.delete(ids);
    }
}
