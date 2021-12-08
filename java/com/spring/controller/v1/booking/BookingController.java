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
			throw new NotFoundException("Nha sĩ đã bận, vui lòng chọn thời gian khác");
		}
		System.out.println("check 2");
		response.setData(this.bookingService.create(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	//cập nhật thời gian, có check trùng lịch
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
			throw new NotFoundException("Lịch khám đã tồn tại, vui lòng chọn thời gian khác");
		}
		response.setData(this.bookingService.update(bookingDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	//cập nhật ghi chú khi hủy lịch,cập nhật kết quả
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

	// cập nhật status 0-dang cho 1-dat lich thanh cong 2-dat lich that bai
	@PutMapping("/{idBooking}/status/{status}")
	public ResponseEntity<Response<BookingDTO>> updateStatus(@PathVariable("idBooking") Long id,
			@PathVariable("status") Integer status) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.updateStatus(id, status));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// lay booking theo scheduletime id (check trùng)
	@GetMapping("/scheduleTime/{id}")
	public ResponseEntity<Response<BookingDTO>> checkScheduleTime(@PathVariable("id") Long id) {
		Response<BookingDTO> response = new Response<>();
		response.setData(bookingService.findByScheduleTime(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// lấy booking theo acc customer (người dùng xem đặt lịch của mình)
	@GetMapping("/customerId/{id}")
	public ResponseEntity<Response<List<BookingDTO>>> getAllByCustomerId(@PathVariable("id") Long id) {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findByCustomerId(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// lấy booking theo acc dentist (bác sĩ xem công việc của mình)
	@GetMapping("/dentistId/{id}")
	public ResponseEntity<Response<List<BookingDTO>>> getAllByDentistId(@PathVariable("id") Long id) {
		Response<List<BookingDTO>> response = new Response<>();
		response.setData(bookingService.findByDentistId(id));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}