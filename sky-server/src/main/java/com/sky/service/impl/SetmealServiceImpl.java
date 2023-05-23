package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto,setmeal);
        setmealMapper.insert(setmeal);
        //添加套餐包含的菜品信息到数据库中
        for (SetmealDish setmealDish : dto.getSetmealDishes()) {
            setmealDishMapper.insert(new SetmealDish(dto.getId()
                            ,setmeal.getId()
                            ,setmealDish.getDishId()
                            ,setmealDish.getName()
                            ,setmealDish.getPrice()
                            ,setmealDish.getCopies()));
        }
    }
    /**
     * 套餐分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult selectByPage(SetmealPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<SetmealVO> page = setmealMapper.selectByPage(dto);
        PageResult pageResult = PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
        return pageResult;
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectById(Long id) {
        SetmealVO setmealVO = setmealMapper.selectById(id);
        //根据套餐id获取套餐内包含的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }
    /**
     * 修改套餐
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(SetmealDTO dto) {
        //删除套餐原有的菜品信息
        setmealDishMapper.deleteById(dto.getId());
        //修改套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto,setmeal);
        setmealMapper.updateInfo(setmeal);
        //添加新的菜品信息
        for (SetmealDish setmealDish : dto.getSetmealDishes()) {
            setmealDishMapper.insert(new SetmealDish(dto.getId()
                    ,setmeal.getId()
                    ,setmealDish.getDishId()
                    ,setmealDish.getName()
                    ,setmealDish.getPrice()
                    ,setmealDish.getCopies()));
        }
    }

    /**
     * 套餐删除
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Integer[] ids) {
        //根据id删除套餐
        setmealMapper.deleteByIds(ids);
        //根据套餐id删除关联的菜品信息
        setmealDishMapper.deleteByIds(ids);
    }
    /**
     * 套餐状态修改
     * @param status
     * @param id
     * @return
     */
    @Override
    public void disableStatus(Integer status,Integer id) {
        setmealMapper.disableStatus(status,id);
    }
}
