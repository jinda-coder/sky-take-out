package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.admin.ReportService;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "报表相关接口")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("接收到的日期区间为{}到{}",begin,end);
        TurnoverReportVO vo = reportService.turnoverStatistics(begin,end);
        return Result.success(vo);
    }
}
