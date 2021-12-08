package com.spring.service.reportServices;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ReportServices {
	List<com.spring.dto.model.ReportServices> findAllReport();
}
