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
    public Setmeal selectById(Long id) {
        return setmealMapper.selectById(id);
    }
    /**
     * 修改套餐
     * @param dto
     * @return
     */
    @Override
    public void updateInfo(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto,setmeal);
        setmealMapper.updateInfo(setmeal);
    }
}
