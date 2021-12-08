package com.spring.repository;

import com.spring.model.Districts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictsRepository extends JpaRepository<Districts, String> {
	@Query(value = "select d from Districts d where d.provinces.id = ?1")
	List<Districts> findByProvinces(String id);
}
