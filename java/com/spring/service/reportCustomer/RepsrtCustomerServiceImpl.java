package com.spring.service.reportCustomer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportCustommer;
import com.spring.repository.CustomerProfileRepository;

@Service
public class RepsrtCustomerServiceImpl implements ReportCustomerService{

	@Autowired
	private CustomerProfileRepository customerProfileRepository ;
	
	
	@Override
	public List<ReportCustommer> report() {
		List<ReportCustommer> cds = new ArrayList<>();
		customerProfileRepository.report().forEach(m -> {
			cds.add(new ReportCustommer(String.valueOf(m.get("fullname")), Integer.parseInt(String.valueOf(m.get("solandat")))));
		});
		return cds;
	}

}
