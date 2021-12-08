package com.spring.service.reportDentist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.model.ReportDentist;
import com.spring.repository.CustomerProfileRepository;
//import com.spring.repository.ReportDentistReponse;
import com.spring.repository.DentistProfileRepository;

@Service
public class ReportDentistServiceImpl implements ReportDentistService {
	@Autowired
	DentistProfileRepository dentistProfileRepository;
	@Override
	public List<ReportDentist> findAllCustomer() {
		List<ReportDentist> cds = new ArrayList<>();
		dentistProfileRepository.report().forEach(
		m -> {
			cds.add(new ReportDentist(String.valueOf(m.get("fullname")),Integer.parseInt(String.valueOf(m.get("solandat")))));
		}
		);
		
		return cds;
	}
}


//List<ReportDentist> cds = customerProfileRepository.test().stream().map(m -> {
//	ReportDentist rd = new ReportDentist();
//	rd.setTenBacSi(String.valueOf(m.get("fullname")));
//	rd.setTongSoDon(Integer.parseInt(String.valueOf(m.get("solandat"))));
////	Cd cd = new Cd();
////	cd.setId(Long.parseLong(String.valueOf(m.get("id"))));
////	cd.setTitle(String.valueOf(m.get("title")));
////	cd.setArtist(String.valueOf(m.get("artist")));
////	return cd;
//}).collect(Collectors.toList());

//List<ReportDentist> d = new ReportDentist();
//return reportDentistReponse.findAllCustomer();
//EntityManagerFactory emf = Persistence.createEntityManagerFactory("graduation_pro_1");
//EntityManager em = emf.createEntityManager();
//List<ReportDentist> bvs = em.createQuery("SELECT v FROM reportAllDentist v", ReportDentist.class).getResultList();
	
//return bvs;
//EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("webqlnk");
//EntityManager entityManager = entityManagerFactory.createEntityManager();
//List<ReportDentist> bvs = entityManager.createQuery("SELECT v FROM reportAllDentist v", ReportDentist.class).getResultList();
