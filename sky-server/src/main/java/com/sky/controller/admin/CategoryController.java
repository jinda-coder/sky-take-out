package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> selectByPage(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.selectByPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    @GetMapping("/list")
    public Result list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }

    /**
     * 添加分类
     * @param dto
     * @return
     */
    @PostMapping
    public Result add(@RequestBody CategoryDTO dto){
        categoryService.add(dto);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Integer id){
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 修改菜品分类
     * @param dto
     * @return
     */
    @PutMapping
    public Result updateInfo(@RequestBody CategoryDTO dto){
        categoryService.updateInfo(dto);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    public Result disable(@PathVariable() Integer status, @Param("id") Long id){
        categoryService.disable(status,id);
        return Result.success();
    }
}
