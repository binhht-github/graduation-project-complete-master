package com.spring.repository;

import com.spring.model.ScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleTimeRepository extends JpaRepository<ScheduleTime, Long> {
  //read all by deleteAt=TRUE
    public List<ScheduleTime> findByDeleteAtIsTrue();

    //read all by deleteAT=FALSE
    public List<ScheduleTime> findByDeleteAtIsFalse();

    @Query("SELECT e FROM ScheduleTime e WHERE e.dentistProfile.id=:dentistProfileId AND " +
            "e.deleteAt=FALSE AND e.dayOfWeek > CURRENT_DATE GROUP BY e.dayOfWeek")
    public List<ScheduleTime> findAllTimeByDentistId(@Param("dentistProfileId") Long dentistProfileId);

    @Query("SELECT e FROM ScheduleTime e WHERE e.dayOfWeek=:dayOfWeek  " +
            "AND e.dentistProfile.id=:dentistId AND e.deleteAt=FALSE")
        public List<ScheduleTime> findHourByDayAndDentistId
            (@Param("dayOfWeek") LocalDate dayOfWeek, @Param("dentistId") Long dentistId);
}
