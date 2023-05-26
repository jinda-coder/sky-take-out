package com.sky.controller.admin;

import com.sky.constant.ShopConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/shop")
@RestController
@Api("店铺相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result updateStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set(ShopConstant.SHOP_STATUS_KEY,status);
        return Result.success();
    }
    @GetMapping("/status")
    public Result checkStatus(){
        return Result.success(redisTemplate.opsForValue().get(ShopConstant.SHOP_STATUS_KEY));
    }
}
