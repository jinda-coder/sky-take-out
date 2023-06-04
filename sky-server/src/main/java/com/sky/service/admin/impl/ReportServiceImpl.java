package com.sky.service.admin.impl;

import com.sky.exception.BaseException;
import com.sky.mapper.ReportMapper;
import com.sky.service.admin.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
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
            Double sum = reportMapper.getByDate(date);
            turnovers.add(String.valueOf(sum == null ? 0 : sum));
        });
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .turnoverList(StringUtils.join(turnovers,","))
                .build();
    }
}
