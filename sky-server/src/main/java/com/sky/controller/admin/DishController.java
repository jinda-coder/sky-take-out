package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result save(@RequestBody DishDTO dto){
        dto.setStatus(1);
        dishService.save(dto);
        return Result.success();
    }

    /**
     * 菜品分类分页查询
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> selectByPage(DishPageQueryDTO dto){
        PageResult pageResult = dishService.selectByPage(dto);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品删除的id:{}",ids);
        dishService.delete(ids);
        return Result.success();
    }
    /**
     * 菜品修改功能
     * @param dto
     */
    @PutMapping
    public Result updateInfo(@RequestBody DishDTO dto){
        dishService.updateInfo(dto);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id){
        return Result.success(dishService.selectById(id));
    }
    @PostMapping("/status/{status}")
    public Result disable(@PathVariable Integer status,@Param("id") Long id){
        dishService.updateStatus(status,id);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<Dish>> findByCategoryId(Long categoryId){
        List<Dish> list = dishService.findByCategoryId(categoryId);
        return Result.success(list);
    }
}
