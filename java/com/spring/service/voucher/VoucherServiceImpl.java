package com.spring.service.voucher;

import com.spring.dto.model.CustomerProfileDTO;
import com.spring.dto.model.VoucherDTO;
import com.spring.dto.response.Response;
import com.spring.exception.NotFoundException;
import com.spring.model.Voucher;
import com.spring.repository.VoucherRepository;
import com.spring.service.customer.CustomerService;
import com.spring.service.email.MailServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;

@Service
public class VoucherServiceImpl implements VoucherService {

	private final VoucherRepository voucherRepository;
	private final CustomerService customerService;

	@Autowired
	public VoucherServiceImpl(VoucherRepository voucherRepository, CustomerService customerService) {
		this.voucherRepository = voucherRepository;
		this.customerService = customerService;
	}

	@Autowired
	MailServices mailServices;

	@Override
	public VoucherDTO save(VoucherDTO dto) {
		dto.setDeleteAt(false);
		return this.voucherRepository.save(dto.convertDTOToEntity()).convertEntityToDTO();
	}

	public String createVoucher(int id) {
		try {
			return id + RandomStringUtils.randomAlphanumeric(5) + new Date().getTime()
					+ RandomStringUtils.randomAlphanumeric(5);
		} catch (Exception e) {
			return null;
		}
	}

	public VoucherDTO sentVoucher(int id, String EmailNhan) {
		VoucherDTO v = null;
		try {
			System.out.println("count id " + voucherRepository.getCountBooking(id));
			if (voucherRepository.getCountBooking(id) == 20) {
				String maVoucher = createVoucher(id);
				v = new VoucherDTO(maVoucher, "CHI ÂN KHÁCH HÀNG JAVA", "ẢNH", 50.0, LocalDateTime.now(),
						LocalDateTime.now().plusMonths(2), new Date(), false);
				if (save(v) != null) {
					mailServices.push(EmailNhan, "Chi ân khách hàng" + v.getContent(),
							"<html><body>Xin cảm ơn quý khách đã sử dụng dịch vụ của phòng khám, phòng khám xin gửi tới quý khách 1 voucher giảm giá 50%"
									+ " <br/> Mã Voucher: " + maVoucher + " </body></html>");
				}
			}
		} catch (Exception e) {
			v = null;
		}

		return v;
	}

	@Override
	public Response<?> sentVoucherByAdmin(Long idCustomer, Double sale) {
		Response<?> response = new Response<>();

		try {
			CustomerProfileDTO cs = customerService.getById(idCustomer);
			String maVoucher = createVoucher(Integer.parseInt(idCustomer + ""));
			VoucherDTO v = new VoucherDTO(maVoucher, "CHI ÂN KHÁCH HÀNG JAVA", "ẢNH", sale, LocalDateTime.now(),
					LocalDateTime.now().plusMonths(2), new Date(), false);
			if (save(v) != null) {
				mailServices.push(cs.getAccounts().getEmail(), "Chi ân khách hàng" + v.getContent(),
						"<html><body>Xin cảm ơn quý khách đã sử dụng dịch vụ của phòng khám, phòng khám xin gửi tới quý khách 1 voucher giảm giá "+sale+"%"
								+ " <br/> Mã Voucher: " + maVoucher + " </body></html>");
			}
			this.sentVoucher(Integer.parseInt(cs.getAccounts().getId() + ""), cs.getAccounts().getEmail());
			response.setErrors("Gửi Voucher Thành công");
		} catch (NotFoundException e) {
			response.setErrors("Gửi Voucher By Admin Thất bại");
			e.printStackTrace();
		}
		return response;
	}



	@Override
	public VoucherDTO update(VoucherDTO dto) {
		dto.setDeleteAt(false);
		return this.voucherRepository.save(dto.convertDTOToEntity()).convertEntityToDTO();
	}

	@Override
	public List<VoucherDTO> findByTitle(String title) {
		List<VoucherDTO> itemDTO = new ArrayList<>();
		this.voucherRepository.findByContentAndDeleteAtIsFalse(title).forEach(t -> itemDTO.add(t.convertEntityToDTO()));
		return itemDTO;

	}

	@Override
	public Optional<VoucherDTO> findByIdAndDeleteAtFalse(String id) {

		Optional<Voucher> voucher = this.voucherRepository.findByIdAndDeleteAtIsFalse(id);
		if (voucher.isPresent()) {
			return voucher.map(Voucher::convertEntityToDTO);
		}
		return Optional.empty();
	}

	@Override
	public Optional<VoucherDTO> findById(String id) {
		Optional<Voucher> voucher = this.voucherRepository.findById(id);
		if (voucher.isPresent()) {
			return voucher.map(Voucher::convertEntityToDTO);
		}
		return Optional.empty();
	}

	@Override
	public List<VoucherDTO> checkExprVoucher(LocalDateTime endDate, LocalDateTime nowDate) {
		Optional<List<Voucher>> listVoucher=
				this.voucherRepository.checkExprVoucher(endDate,nowDate);
		List<VoucherDTO> itemDTO = new ArrayList<>();
		if(listVoucher.isPresent()){
			listVoucher.get().forEach(e->itemDTO.add(e.convertEntityToDTO()));
			return itemDTO;
		}
		return null;
	}



	@Override
	public List<VoucherDTO> findAll() {
		List<VoucherDTO> itemDTO = new ArrayList<>();
		this.voucherRepository.findByDeleteAtIsFalse()
				.forEach(t -> itemDTO.add(t.convertEntityToDTO()));
		return itemDTO;
	}

	@Override
	public List<Voucher> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
		return this.voucherRepository.findAllByStartGreaterThanEqualAndStartLessThanEqualAndDeleteAtIsFalse(startDate, endDate);
	}

	@Override
	public void hardDelete(String id) throws NotFoundException {
		Voucher entity = this.voucherRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Voucher not found with :" + id));
		this.voucherRepository.delete(entity);
	}

	@Override
	public VoucherDTO softDelete(String id) throws NotFoundException {
		Optional<Voucher>  optional = this.voucherRepository.findById(id);
		if(optional.isPresent()){
			Voucher entity= optional.get();
			entity.setDeleteAt(true);
			return this.voucherRepository.save(entity).convertEntityToDTO();
		}
		return null;
	}
}
