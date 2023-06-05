package com.sky.service.admin.impl;

import com.sky.mapper.WorkspaceMapper;
import com.sky.service.admin.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Override
    public BusinessDataVO businessData() {
        //1.营业额
        Double turnover = workspaceMapper.getturnover();
        //2.有效订单数
        Integer validOrderCount = workspaceMapper.getValidOrderCount();
        //3.订单完成率
        //3.1总订单数
        Integer totalCount = workspaceMapper.getTotalCount();
        //订单完成率
        Double orderCompletionRate = (double)validOrderCount/totalCount;
        //4.平均客单价
        //查询下单人数
        Integer userCount = workspaceMapper.getUserCount();
        Double unitPrice = turnover/userCount;
        //5.新增用户数
        Integer newUsers = workspaceMapper.getNewUsers();
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers).build();
    }
}
