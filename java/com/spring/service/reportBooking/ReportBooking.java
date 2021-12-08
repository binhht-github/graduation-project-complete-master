package com.spring.service.reportBooking;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportBookingDTO;

@Service
public interface ReportBooking {
	List<ReportBookingDTO> report();
}
