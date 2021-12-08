package com.spring.service.reportThongke;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportThongKeDTO;

@Service
public interface ReportThongKe {
	List<ReportThongKeDTO> report();
}
