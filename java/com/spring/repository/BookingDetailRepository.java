package com.spring.repository;

import com.spring.dto.model.ReportThangDTO;
import com.spring.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
	@Query("select bdt from BookingDetail bdt where bdt.booking.id=?1")
	List<BookingDetail> findBookingDetailByBookingId(Long id);

	@Query(value="call reportThang(?1)",nativeQuery = true)
	List<Object[]> report(Integer nam);
}
