package com.spring.repository;

import com.spring.model.Communes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunesRepository extends JpaRepository<Communes, String> {
	@Query(value = "select c from Communes c where c.districts.id = ?1")
	List<Communes> findByDistrictsCustomer(String id);
}
