package com.spring.service.reportDentist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportDentist;

@Service
public interface ReportDentistService {
	List<ReportDentist> findAllCustomer();

}
