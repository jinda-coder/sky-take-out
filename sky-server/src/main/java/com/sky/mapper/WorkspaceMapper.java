package com.sky.mapper;

import com.sky.vo.BusinessDataVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkspaceMapper {
    Double getTurnover();

    Integer getValidOrderCount();

    Integer getTotalCount();

    Integer getUserCount();

    Integer getNewUsers();

    Integer getOrderCountByStatus(Integer status);

    Integer getDishesByStatus(Integer status);
}
