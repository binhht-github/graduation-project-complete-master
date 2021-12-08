package com.spring.service.reportThang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportThangDTO;
import com.spring.repository.BookingDetailRepository;

@Service
public class ReportThangImpl implements reportThang {
	@Autowired
	BookingDetailRepository bookingDetailRepository;
	@Override
	public List<ReportThangDTO> report(Integer nam) {
		List<ReportThangDTO> list = new ArrayList<>();

		bookingDetailRepository.report(nam).forEach(obj -> {
			list.add(new ReportThangDTO(obj[0]+"", Double.parseDouble(obj[1]+""))); 
		});
		return list;
	}

}
