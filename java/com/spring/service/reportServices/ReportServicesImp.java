package com.spring.service.reportServices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.repository.ServiceRepository;

@Service
public class ReportServicesImp implements ReportServices {
	@Autowired
	ServiceRepository serviceRepository;

	@Override
	public List<com.spring.dto.model.ReportServices> findAllReport() {
		List<com.spring.dto.model.ReportServices> cds = new ArrayList<>();
		serviceRepository.report().forEach(m -> {
			cds.add(new com.spring.dto.model.ReportServices(String.valueOf(m.get("tenDV")),
					Integer.parseInt(String.valueOf(m.get("sl")))));
		});
		return cds;
	}

}
