package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface ReportMapper {
    @Select("select sum(amount) from orders where status = 5 and date_format(order_time,'%Y-%m-%d') = #{date}")
    Double getTurnoversByDate(String date);
    @Select("select round(count(if(status = 5, 1, null))/count(*) * 100, 2) as match_percent\n" +
            "from orders where date_format(order_time,'%Y-%m-%d') between #{begin} and #{end}")
    Double getOrderCompletionRateByDate(@Param("begin") LocalDate begin, @Param("end") LocalDate end);
    @Select("select count(*) from orders where date_format(order_time,'%Y-%m-%d') = #{date}")
    Integer getOrderCountByDate(String d);
    @Select("select count(*) from orders where date_format(order_time,'%Y-%m-%d') between #{begin} and #{end}")
    Integer getAllOrderCountByDate(@Param("begin") LocalDate begin, @Param("end") LocalDate end);
    @Select("select count(*) from orders where status = 5 and date_format(order_time,'%Y-%m-%d') between #{begin} and #{end}")
    Integer getValidOrderByDate(@Param("begin") LocalDate begin, @Param("end")LocalDate end);
    @Select("select count(*) from orders where status = 5 and date_format(order_time,'%Y-%m-%d') = #{date}")
    Integer getValidOrderCountListByDate(String date);
}
