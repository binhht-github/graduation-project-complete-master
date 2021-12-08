package com.spring.service.reportBooking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportBookingDTO;
import com.spring.repository.BookingRepository;

@Service
public class ReportBookingImpl implements ReportBooking {
	@Autowired
	BookingRepository bookingRepository;

	@Override
	public List<ReportBookingDTO> report() {
		// TODO Auto-generated method stub
		List<ReportBookingDTO> list = new ArrayList<>();	
		bookingRepository.report().forEach(obj ->{
			list.add(new ReportBookingDTO(Integer.parseInt(obj[0] + ""), Integer.parseInt(obj[1] + ""), Integer.parseInt(obj[2] + ""), Integer.parseInt(obj[3] + "")));
		});
		return list;
	}

}
