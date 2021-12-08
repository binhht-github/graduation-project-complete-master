package com.spring.controller.v1.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.model.ReportBookingDTO;
import com.spring.dto.model.ReportCustommer;
import com.spring.dto.model.ReportDentist;
import com.spring.dto.model.ReportThangDTO;
import com.spring.dto.model.ReportThangHoaDon;
import com.spring.dto.model.ReportThongKeDTO;
import com.spring.dto.response.Response;
import com.spring.service.reportBooking.ReportBooking;
import com.spring.service.reportCustomer.ReportCustomerService;
import com.spring.service.reportDentist.ReportDentistService;
import com.spring.service.reportServices.ReportServices;
import com.spring.service.reportThang.reportThang;
import com.spring.service.reportThangHoaDon.reportHoaDon;
import com.spring.service.reportThongke.ReportThongKe;

@RestController
@RequestMapping("/api/v1/report")
public class Report {
	@Autowired
	ReportServices reportServices;
	@Autowired
	ReportDentistService reportDentistService;
	@Autowired
	ReportCustomerService reportCustomerService;
	@Autowired
	ReportBooking reportBooking;
	@Autowired
	ReportThongKe reportThongKe;
	@Autowired
	reportThang reportThangservices;
	@Autowired
	reportHoaDon reportHoaDon;
	
	// trả về danh sách bác sĩ và số người đặt bắc sĩ
	@GetMapping("/dentist")
	public ResponseEntity<Response<List<ReportDentist>>> reportCustomer() {
		Response<List<ReportDentist>> response = new Response<>();
		response.setData(reportDentistService.findAllCustomer());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	// trả về danh sách serices - sl đặt
	@GetMapping("/services")
	public ResponseEntity<Response<List<com.spring.dto.model.ReportServices>>> reportServices() {
		Response<List<com.spring.dto.model.ReportServices>> response = new Response<>();
		response.setData(reportServices.findAllReport());
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	// trả về danh sách khách hàng và số lần đặt
	@GetMapping("/customer")
	public ResponseEntity<Response<List<ReportCustommer>>> reportDentist() {
		Response<List<ReportCustommer>> response = new Response<>();
		response.setData(reportCustomerService.report());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/booking")
	public ResponseEntity<Response<List<ReportBookingDTO>>> reportBooking() {
		Response<List<ReportBookingDTO>> response = new Response<>();
		response.setData(reportBooking.report());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/doanhthu")
	public ResponseEntity<Response<List<ReportThongKeDTO>>> reportDoanhThu() {
		Response<List<ReportThongKeDTO>> response = new Response<>();
		response.setData(reportThongKe.report());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/doanhthu/{nam}")
	public ResponseEntity<Response<List<ReportThangDTO>>> reportDoanhThu(@PathVariable("nam") int nam) {
		Response<List<ReportThangDTO>> response = new Response<>();
		response.setData(reportThangservices.report(nam));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("/hoadon")
	public ResponseEntity<Response<List<ReportThangHoaDon>>> reportHoaDon() {
		Response<List<ReportThangHoaDon>> response = new Response<>();
		response.setData(reportHoaDon.report());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
