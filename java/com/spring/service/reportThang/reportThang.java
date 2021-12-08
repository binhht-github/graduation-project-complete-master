package com.spring.service.reportThang;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportThangDTO;

@Service
public interface reportThang {
	List<ReportThangDTO> report(Integer nam);
}
