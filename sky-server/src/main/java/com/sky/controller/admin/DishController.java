package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
