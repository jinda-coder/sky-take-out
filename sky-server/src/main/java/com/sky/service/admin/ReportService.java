package com.sky.service.admin;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

}
