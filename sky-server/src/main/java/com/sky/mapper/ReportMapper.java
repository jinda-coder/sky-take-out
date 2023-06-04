package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReportMapper {
    @Select("select sum(amount) from orders where status = 5 and date_format(order_time,'%Y-%m-%d') = #{date}")
    Double getByDate(String date);

}
