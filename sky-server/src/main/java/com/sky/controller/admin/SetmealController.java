package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param dto
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    public Result add(@RequestBody SetmealDTO dto){
        setmealService.add(dto);
        return Result.success();
    }
    /**
     * 套餐分页查询
     * @param dto
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> selectByPage(SetmealPageQueryDTO dto){
        PageResult pageResult = setmealService.selectByPage(dto);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> selectById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.selectById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result updateInfo(@RequestBody SetmealDTO dto){
        setmealService.updateInfo(dto);
        return Result.success();
    }

    /**
     * 套餐删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteByIds(Integer[] ids){
        log.info("接受到的参数为{}",ids);
        setmealService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 套餐状态修改
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("套餐状态修改")
    @PostMapping("/status/{status}")
    public Result disableStatus(@PathVariable Integer status,@Param("id") Integer id){
        setmealService.disableStatus(status,id);
        return Result.success();
    }
}
