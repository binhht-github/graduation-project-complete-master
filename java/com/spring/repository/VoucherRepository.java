package com.spring.repository;

import com.spring.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
	@Query(value = "select getCountBooking(?1)", nativeQuery = true)
	int getCountBooking(int idCustommer);

	List<Voucher> findAllByStartGreaterThanEqualAndStartLessThanEqualAndDeleteAtIsFalse
			(LocalDateTime startDate, LocalDateTime endDate);

	List<Voucher> findByContentAndDeleteAtIsFalse(String title);

	//find All
	List<Voucher> findByDeleteAtIsFalse();

	//find by Id
	Optional<Voucher> findByIdAndDeleteAtIsFalse(String id);



	//check exp voucher
	@Query("SELECT e FROM Voucher e WHERE e.end=?1 AND e.end>=?2 AND e.deleteAt=false ")
	Optional<List<Voucher>>  checkExprVoucher(LocalDateTime endDate, LocalDateTime nowDate);
}
