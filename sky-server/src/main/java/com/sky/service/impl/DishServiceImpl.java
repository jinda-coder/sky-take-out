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

    /**
     * 菜品修改功能
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(DishDTO dto){
        //若前端提交了修改请求 先删除原先菜品的口味信息
        dishFlavorMapper.delete(dto.getId());
        //然后根据接收到的前端数据修改数据库信息
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dto,dishVO);
        dishMapper.updateInfo(dishVO);
        //插入最新的口味数据
        dto.getFlavors().forEach(f->{f.setDishId(dto.getId());});
        dishFlavorMapper.insert(dishVO.getFlavors());
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DishVO selectById(Long id){
        Dish dish = dishMapper.selectById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 菜品状态修改
     * @param status
     */
    @Override
    public void updateStatus(Integer status,Long id) {
        dishMapper.updateStatus(status,id);
    }
}
