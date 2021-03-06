package com.spring.controller.v1.booking;

import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.model.BookingDTO;
import com.spring.dto.response.Response;
import com.spring.exception.NotFoundException;
import com.spring.service.booking.BookingService;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	// get all booking
	@GetMapping()
	public ResponseEntity<Response<List<BookingDTO>>> getAll() {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findAll());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// get one booking by id
	@GetMapping("{id}")
	public ResponseEntity<Response<BookingDTO>> getOne(@PathVariable("id") Long id) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.findById(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','CUSTOMER')")
	@PostMapping()
	public ResponseEntity<Response<BookingDTO>> create(@RequestBody @Valid BookingDTO bookingDTO,
			BindingResult bindingResult) throws NotFoundException, MessagingException {
		Response<BookingDTO> response = new Response<>();
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		System.out.println("check 1");
		if (this.bookingService
				.checkIfScheduleTimeExists(bookingDTO.getDentistProfile().getId(), bookingDTO.getScheduleTime().getId())
				.isPresent()) {
			throw new NotFoundException("Nha s?? ???? b???n, vui l??ng ch???n th???i gian kh??c");
		}
		System.out.println("check 2");
		response.setData(this.bookingService.create(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	//c???p nh???t th???i gian, c?? check tr??ng l???ch
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{id}")
	public ResponseEntity<Response<BookingDTO>> update(@PathVariable("id") Long id,
			@RequestBody @Valid BookingDTO bookingDTO, BindingResult bindingResult)
			throws NotFoundException, MessagingException{
		Response<BookingDTO> response = new Response<>();
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		if(this.bookingService.checkIfScheduleTimeExists(bookingDTO.getDentistProfile().getId(),
				bookingDTO.getScheduleTime().getId()).isPresent()){
			throw new NotFoundException("L???ch kh??m ???? t???n t???i, vui l??ng ch???n th???i gian kh??c");
		}
		response.setData(this.bookingService.update(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//c???p nh???t ghi ch?? khi h???y l???ch,c???p nh???t k???t qu???
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/ghi-chu/{id}")
	public ResponseEntity<Response<BookingDTO>> updateGhiChu
			(@PathVariable("id") Long id,
			 @RequestBody @Valid BookingDTO bookingDTO,
			 BindingResult bindingResult)
			throws NotFoundException, MessagingException{
		Response<BookingDTO> response = new Response<>();
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.bookingService.update(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// c???p nh???t status 0-dang cho 1-dat lich thanh cong 2-dat lich that bai
	@PutMapping("/{idBooking}/status/{status}")
	public ResponseEntity<Response<BookingDTO>> updateStatus(@PathVariable("idBooking") Long id,
			@PathVariable("status") Integer status) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.updateStatus(id, status));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// lay booking theo scheduletime id (check tr??ng)
	@GetMapping("/scheduleTime/{id}")
	public ResponseEntity<Response<BookingDTO>> checkScheduleTime(@PathVariable("id") Long id) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.findByScheduleTime(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// l???y booking theo acc customer (ng?????i d??ng xem ?????t l???ch c???a m??nh)
	@GetMapping("/customerId/{id}")
	public ResponseEntity<Response<List<BookingDTO>>> getAllByCustomerId(@PathVariable("id") Long id) {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findByCustomerId(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// l???y booking theo acc dentist (b??c s?? xem c??ng vi???c c???a m??nh)
	@GetMapping("/dentistId/{id}")
	public ResponseEntity<Response<List<BookingDTO>>> getAllByDentistId(@PathVariable("id") Long id) {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findByDentistId(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}