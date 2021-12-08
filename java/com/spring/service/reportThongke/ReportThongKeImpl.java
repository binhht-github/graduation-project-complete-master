package com.spring.service.reportThongke;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.dto.model.ReportThongKeDTO;
import com.spring.repository.BookingRepository;

@Service
public class ReportThongKeImpl implements ReportThongKe {
	@Autowired
	BookingRepository bookingRepository;

	@Override
	public List<ReportThongKeDTO> report() {
		List<ReportThongKeDTO> list = new ArrayList<>();
		
		bookingRepository.reportDoanhThu().forEach(obj -> {
			list.add(new ReportThongKeDTO(obj[0] + "", Double.parseDouble(obj[1] + "")));
		});
		return list;
	}

}
