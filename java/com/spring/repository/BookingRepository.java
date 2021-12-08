package com.spring.repository;

import com.spring.model.Accounts;
import com.spring.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select bk from Booking bk where bk.scheduleTime.id=?1")
    Optional<Booking> findByScheduleTimeId(Long id);

    @Query("select bk from Booking bk where bk.customerProfile.id=?1")
    List<Booking> findByCustomerId(Long id);

    @Query("select bk from Booking bk where bk.dentistProfile.id=?1")
    List<Booking> findByDentistId(Long id);
    //check trùng lịch
    //<> not equals, <> tương đương với !=
    @Query("SELECT bk FROM Booking bk WHERE bk.dentistProfile.id=?1 " +
            "AND bk.scheduleTime.id=?2 AND bk.status<>3")
    Optional<Booking> checkScheduleTimeExists(Long dentistId,Long scheduleTimeId);
    
    @Query(value = "select * from CountBooking", nativeQuery = true)
    List<Object[]> report();
    
    @Query(value = "select * from reportDoanhThu", nativeQuery = true)
    List<Object[]> reportDoanhThu();
    
    @Query(value = "select * from reportThangHoadon", nativeQuery = true)
    List<Object[]> reportHoaDon();
    
    @Query(value = "call exportHoaDon(?1)", nativeQuery = true)
    List<Object[]> exportHoaDon(Long id);
    
    @Query(value = "call exportHoaDon2(?1)", nativeQuery = true)
    List<Object[]> exportHoaDon2(Long id);
}
