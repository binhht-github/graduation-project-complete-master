package com.spring.service.reportThangHoaDon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportThangHoaDon;
import com.spring.repository.BookingRepository;

@Service
public class ReportHoaDonImpl implements reportHoaDon{

	@Autowired
	BookingRepository bookingRepository;
	
	@Override
	public List<ReportThangHoaDon> report() {
		List<ReportThangHoaDon> list = new ArrayList<>();

		bookingRepository.reportHoaDon().forEach(obj -> {
			list.add(new ReportThangHoaDon(obj[0]+"", Integer.parseInt(obj[1]+""))); 
		});
		
		return list;
	}

}
