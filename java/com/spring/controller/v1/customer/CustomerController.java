package com.spring.controller.v1.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.model.CustomerProfileDTO;
import com.spring.dto.response.Response;
import com.spring.exception.NotFoundException;
import com.spring.service.customer.CustomerService;
import com.spring.service.role.RoleService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	private final CustomerService customerService;
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	RoleService roleService;

	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	
	@GetMapping("")
	public ResponseEntity<Response<List<CustomerProfileDTO>>> getAllCustomer() {
		Response<List<CustomerProfileDTO>> response = new Response<>();
		response.setData(customerService.getAll());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("accounts/{id}")
	public ResponseEntity<Response<CustomerProfileDTO>> getCustomerByAccountId(@PathVariable("id") Long id) throws NotFoundException {
		Response<CustomerProfileDTO> response = new Response<>();
		response.setData(this.customerService.getByAccountId(id));
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','CUSTOMER')")
	@GetMapping("/{id}")
	public ResponseEntity<Response<CustomerProfileDTO>> getCustomer(@PathVariable("id") Long id) throws NotFoundException {
		Response<CustomerProfileDTO> response = new Response<>();
		response.setData(this.customerService.getById(id));
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','CUSTOMER')")
	@PostMapping()
	public ResponseEntity<Response<CustomerProfileDTO>> createCustomer(@Valid @RequestBody CustomerProfileDTO dto,
			BindingResult result) throws NotFoundException {
		Response<CustomerProfileDTO> response = new Response<>();
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(this.customerService.add(dto));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','CUSTOMER')")
	@PutMapping("/{id}")
	public ResponseEntity<Response<CustomerProfileDTO>> updateCustomer(@PathVariable("id") Long id,
			@Valid @RequestBody CustomerProfileDTO dto, BindingResult result) throws NotFoundException {
		System.out.println("update customer");
		Response<CustomerProfileDTO> response = new Response<>();
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(this.customerService.update(id, dto));
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','CUSTOMER')")
	@PatchMapping("/{id}")
	public void sortDeleteDentist(@PathVariable("id") Long id, @RequestParam Boolean deleted) throws NotFoundException {
		customerService.updateDeleted(id, deleted);
	}

	@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable("id") Long id) throws NotFoundException {
		customerService.deleteById(id);
	}
}
