package com.spring.service.reportThangHoaDon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportThangHoaDon;

@Service
public interface reportHoaDon {
	List<ReportThangHoaDon> report();
}
