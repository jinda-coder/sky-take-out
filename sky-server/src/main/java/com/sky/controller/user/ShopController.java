package com.sky.controller.user;

import com.sky.constant.ShopConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api("用户端店铺相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result checkStatus(){
        return Result.success(redisTemplate.opsForValue().get(ShopConstant.SHOP_STATUS_KEY));
    }
}
