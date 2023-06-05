package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.admin.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Auth:ruYan
 * 工作台controller
 */
@RequestMapping("/admin/workspace/")
@RestController
@Api(tags = "工作台相关接口")
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 今日运营数据
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(){
        BusinessDataVO businessDataVO =  workspaceService.businessData();
        return Result.success(businessDataVO);
    }

    /**
     * 今日订单数据
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders(){
        OrderOverViewVO orderOverViewVO = workspaceService.overviewOrders();
        return Result.success(orderOverViewVO);
    }

    /**
     * 菜品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes(){
        DishOverViewVO vo = workspaceService.getOverviewDishes();
        return Result.success(vo);
    }
}
