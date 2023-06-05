package com.sky.service.admin.impl;

import com.sky.exception.BaseException;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.admin.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //判断日期格式是否正确
        if (begin.isAfter(end)){
            throw new BaseException("日期格式非法");
        }
        List<String> dates = new ArrayList<>();
        while(!begin.isAfter(end)){
            dates.add(begin.toString());
            begin = begin.plusDays(1L);
        }
        List<String> turnovers = new ArrayList<>();
        dates.forEach(date->{
            //根据日期查询当日营业额
            Double sum = reportMapper.getTurnoversByDate(date);
            turnovers.add(String.valueOf(sum == null ? 0 : sum));
        });
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .turnoverList(StringUtils.join(turnovers,","))
                .build();
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        LocalDate initialTime = begin;
        if (begin.isAfter(end)){
            throw new BaseException("日期格式非法");
        }
        List<String> dates = new ArrayList<>();
        while(!begin.isAfter(end)){
            dates.add(begin.toString());
            begin = begin.plusDays(1L);
        }
        //1.日期列表
        String dateList = StringUtils.join(dates, ",");
        //根据日期查询当日订单数
        List<String> ordersCountList = new ArrayList<>();
        dates.forEach(date->{
            Integer sum = reportMapper.getOrderCountByDate(date);
            ordersCountList.add(String.valueOf(sum));
        });
        //2.生成订单数列表
        String orderCountList = StringUtils.join(ordersCountList, ",");
        //3.订单总数
        Integer totalOrderCount = reportMapper.getAllOrderCountByDate(initialTime,end);
        //4.有效订单数
        Integer validOrderCount = reportMapper.getValidOrderByDate(initialTime,end);
        //5.有效订单数列表
        List<String> validOrdersCountList = new ArrayList<>();
        dates.forEach(date->{
            Integer sum = reportMapper.getValidOrderCountListByDate(date);
            validOrdersCountList.add(String.valueOf(sum));
        });
        String validOrderCountList = StringUtils.join(validOrdersCountList, ",");
        //6.订单完成率 有效除以总数
        Double orderCompletionRate = (double) validOrderCount/totalOrderCount;
        //7.封装对象返回
        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(dateList)
                .orderCountList(orderCountList)
                .validOrderCountList(validOrderCountList)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
        return orderReportVO;
    }
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<SalesTop10ReportVO> salesTop10ReportVOS = reportMapper.top10(begin, end);
        List<String> nameList = new ArrayList<>();
        List<String> numberList = new ArrayList<>();
        salesTop10ReportVOS.forEach(salesTop10ReportVO -> {
            nameList.add(salesTop10ReportVO.getNameList());
            numberList.add(salesTop10ReportVO.getNumberList());
        });
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }
    /**
     * 根据时间区间统计用户数量
     * @param beginTime
     * @param endTime
     * @return
     */
    public Integer getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("begin",beginTime);
        map.put("end", endTime);
        return userMapper.countByMap(map);
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUserList = new ArrayList<>(); //新增用户数
        List<Integer> totalUserList = new ArrayList<>(); //总用户数

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //新增用户数量 select count(id) from user where create_time > ? and create_time < ?
            Integer newUser = getUserCount(beginTime, endTime);
            //总用户数量 select count(id) from user where  create_time < ?
            Integer totalUser = getUserCount(null, endTime);

            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .build();
    }
}
