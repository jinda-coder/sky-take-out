package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.user.ShoppingCartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api("C端-购物车接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车添加商品
     * @param dto
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO dto){
        shoppingCartService.add2ShoppingCart(dto);
        return Result.success();
    }

    /**
     * 查看购物车商品
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        return Result.success(shoppingCartService.list());
    }
    @DeleteMapping("clean")
    public Result clean(){
        shoppingCartService.clean();
        return Result.success();
    }
}
