package com.spring.service.booking;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dto.model.AccountsDTO;
import com.spring.dto.model.BookingDTO;
import com.spring.dto.model.CustomerProfileDTO;
import com.spring.exception.NotFoundException;
import com.spring.model.Booking;
import com.spring.repository.BookingRepository;
import com.spring.service.account.AccountService;
import com.spring.service.customer.CustomerService;
import com.spring.service.email.MailServices;
import com.spring.service.voucher.VoucherServiceImpl;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	VoucherServiceImpl voucherServiceImpl;
	@Autowired
	AccountService accountService;
	@Autowired
	CustomerService customerService;
	@Autowired
	MailServices mailServices;

	@Override
	public List<BookingDTO> findAll() {
		List<BookingDTO> dtoList = new ArrayList<>();
		List<Booking> entityList = bookingRepository.findAll();
		for (Booking entity : entityList) {
			BookingDTO dto = entity.convertEntityToDTO();
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public BookingDTO findById(Long id) {
		Optional<Booking> optional = bookingRepository.findById(id);
		if (optional.isPresent()) {
			Booking entity = optional.get();
			BookingDTO dto = entity.convertEntityToDTO();
			return dto;
		}
		return null;
	}

	@Override
	public BookingDTO create(BookingDTO bookingDTO) {
		Booking entity = bookingDTO.convertDTOToEntity();
		entity.setBookingDate(new Date());
		entity.setStatus(0); // 0-dang cho 1-dat lich thanh cong 2-dat lich that bai
//		bookingDTO.setId(bookingRepository.save(entity).getId());
		bookingDTO = bookingRepository.save(entity).convertEntityToDTO();
		String email = null;
		try {
			email = customerService.getById(this.findById(bookingDTO.getId()).getCustomerProfile().getId())
					.getAccounts().getEmail();
			System.out.println("email " + customerService
					.getById(this.findById(bookingDTO.getId()).getCustomerProfile().getId()).getAccounts().getEmail());
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		mailServices.push(email, "Thư cảm ơn",
				"<html>" + "<body>" + "Quý khách Yêu cầu đặt lịch phòng khám, Xin vui lòng chờ xác nhận của chúng tôi"
						+ "</body>" + "</html>");
		return bookingDTO;
	}

	@Override
	public BookingDTO update(BookingDTO bookingDTO) {

		Optional<Booking> optional = bookingRepository.findById(bookingDTO.getId());
		if (optional.isPresent()) {
			String email = bookingRepository.findById(bookingDTO.getId()).get().
					getCustomerProfile().getAccounts().getEmail();

			Booking oldEntity=optional.get();
			String dayOfWeekOld=oldEntity.getScheduleTime().getDayOfWeek()
					.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			String startOld=oldEntity.getScheduleTime().getStart().
					format(DateTimeFormatter.ofPattern("HH:mm"));
			String endOld=oldEntity.getScheduleTime().getEnd()
					.format(DateTimeFormatter.ofPattern("HH:mm"));

			Boolean check=false;
			if(bookingDTO.getScheduleTime().getId()!=oldEntity.getScheduleTime().getId()){
				check=true;
//				System.out.printf("okkkkkkk change");
			}

			Booking entity = bookingDTO.convertDTOToEntity();
			bookingDTO = bookingRepository.save(entity).convertEntityToDTO();
//			System.out.println(bookingDTO.getScheduleTime().getDayOfWeek()+"aaaaaaaaaa");

			// gửi mail khi thay đổi lịch khám
			if(check==true){
				mailServices.push(email, "Thay đổi lịch khám", "<html>" + "<body>"
						+ "Lịch khám của bạn đã thay đồi từ: <br/>"
						+"Ngày: "+dayOfWeekOld+" "
						+"khung giờ: "+startOld
						+"-"+endOld+"<br/>"
						+" Chuyển sang: <br/>"
						+"<b>Ngày: "+bookingDTO.getScheduleTime().getDayOfWeek().
						format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" "+
						"khung giờ: "+bookingDTO.getScheduleTime().getStart().
						format(DateTimeFormatter.ofPattern("HH:mm"))+
						"-"+bookingDTO.getScheduleTime().getEnd().
						format(DateTimeFormatter.ofPattern("HH:mm"))+
						"</body>" + "</html>");
//				check=false;
			}

			//gửi mail khi có kết quả khám
			if(bookingDTO.getKetqua()!=null && ! bookingDTO.getKetqua().equals("")){
				mailServices.push(email, "Kết quả khám", "<html>" + "<body>"
						+ "Kết quả khám của bạn là: <br/>" +
						bookingDTO.getKetqua()+"<br/>"
						+"Nha sĩ kết luận: "+bookingDTO.getDentistProfile().getFullName()+
						"</body>" + "</html>");
			}


		}
		return bookingDTO;
	}

	@Override
	public BookingDTO findByScheduleTime(Long id) {
		Optional<Booking> optional = bookingRepository.findByScheduleTimeId(id);
		if (optional.isPresent()) {
			Booking entity = optional.get();
			BookingDTO dto = entity.convertEntityToDTO();
			return dto;
		}
		return null;
	}

	@Override
	public List<BookingDTO> findByCustomerId(Long id) {
		List<BookingDTO> dtoList = new ArrayList<>();
		List<Booking> entityList = bookingRepository.findByCustomerId(id);
		for (Booking entity : entityList) {
			BookingDTO dto = entity.convertEntityToDTO();
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public List<BookingDTO> findByDentistId(Long id) {
		List<BookingDTO> dtoList = new ArrayList<>();
		List<Booking> entityList = bookingRepository.findByDentistId(id);
		for (Booking entity : entityList) {
			BookingDTO dto = entity.convertEntityToDTO();
			dtoList.add(dto);
		}
		return dtoList;
	}

	// gửi voucher
	@Override
	public BookingDTO updateStatus(Long id, Integer status) {
		Optional<Booking> optional = bookingRepository.findById(id);
//		System.out.println("Email 123 "+bookingRepository.findById(id).get().getCustomerProfile().getAccounts().getEmail());
		BookingDTO dto = new BookingDTO();
		if (optional.isPresent()) {
			Booking entity = optional.get();
			entity.setStatus(status);
			bookingRepository.save(entity);
			dto = entity.convertEntityToDTO();
			String email = bookingRepository.findById(id).get().getCustomerProfile().getAccounts().getEmail();
			if(dto.getKetqua() == null) {
				dto.setKetqua("");
			}
			if(dto.getGhichu() == null) {
				dto.setGhichu("");
			}
			switch (status) {
				case 1:
					mailServices.push(email, "Phòng Khám Răng", "<html>" + "<body> <b>Thư cảm ơn</b> <br/>"
							+ "Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi <br/> Quý khách đã đặt lịch khám vào ngày : "
							+  new SimpleDateFormat("dd-MM-yyyy").format(dto.getBookingDate())
							+"<br/>Khung giờ khám: "+dto.getScheduleTime().getStart()
							.format(DateTimeFormatter.ofPattern("HH:mm")) +" - "
							+ dto.getScheduleTime().getEnd().format(DateTimeFormatter.ofPattern("HH:mm"))
							+ "<br/> Quý khách nhớ đến khám đúng giờ</body>" + "</html>");
					voucherServiceImpl.sentVoucher(
							Integer.parseInt(
									bookingRepository.findById(id).get().getCustomerProfile().getId() + ""),
							email);
					break;
				case 2:
					mailServices.push(email, "Thư cảm ơn", "<html>" + "<body>"
							+ "Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi, <br/>" +
							"kết quả khám sẽ được cập nhật và thông báo đến bạn sớm nhất !"+
							"</body>" + "</html>");
	//						voucherServiceImpl.sentVoucher(Integer.parseInt(accountsDTO.getId() + ""), accountsDTO.getEmail());
					break;
				case 3:
					mailServices.push(email, "Thông báo",
							"<html>" + "<body>" + "Lịch khám của quý khách vừa bị hủy <br/> Lí do hủy: "+dto.getGhichu() + "</body>" + "</html>");
					break;
				default:
					mailServices.push(email, "Thư cảm ơn",
							"<html>" + "<body>"
									+ "Quý khách Yêu cầu đặt lịch phòng khám, Xin vui lòng chờ xác nhận của chúng tôi"
									+ "</body>" + "</html>");
					break;
			}
		}
		return dto;
	}

	// check trùng lịch
	@Override
	public Optional<Booking> checkIfScheduleTimeExists(Long dentistId, Long scheduleTimeId) {
		return this.bookingRepository.checkScheduleTimeExists(dentistId, scheduleTimeId);
	}
//    @Override
//    public BookingDTO updateStatus(Long id, Integer status) {
//        Optional<Booking> optional = bookingRepository.findById(id);
//        BookingDTO dto = new BookingDTO();
//        if(optional.isPresent()){
//            Booking entity = optional.get();
//            entity.setStatus(status);
//            bookingRepository.save(entity);
//            dto = entity.convertEntityToDTO();
//            if(status == 1) {
//            	BookingDTO b = findById(id);
////            	voucherServiceImpl.sentVoucher(Integer.parseInt(b.getId() + ""), b.getAccountsDTO().getEmail());
//            	voucherServiceImpl.sentVoucher(122, "binhhtph11879@fpt.edu.vn");
//            }
//        }
//        return dto;
//    }

}
