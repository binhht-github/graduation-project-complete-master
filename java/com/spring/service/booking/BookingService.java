package com.spring.service.booking;

import com.spring.dto.model.BookingDTO;
import com.spring.model.*;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<BookingDTO> findAll();

    BookingDTO findById(Long id);

    BookingDTO create(BookingDTO bookingDTO);

    BookingDTO update(BookingDTO bookingDTO);

    BookingDTO findByScheduleTime(Long id);

    List<BookingDTO> findByCustomerId(Long id);

    List<BookingDTO> findByDentistId(Long id);

    BookingDTO updateStatus(Long id, Integer status);
    
  //check trùng lịch
    Optional<Booking> checkIfScheduleTimeExists(Long dentistId, Long scheduleTimeId);
}
