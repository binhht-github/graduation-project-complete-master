package com.spring.repository;

import com.spring.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {
	@Query(value ="select getRoelByAccount(?1)",nativeQuery = true)
	String getRoleById(Long id);
	
    @Query( value = "SELECT a FROM Accounts a WHERE a.email = :keyWord or a.telephone = :keyWord AND a.deleteAt = false ")
    Optional<Accounts> checkIfEmailExistsAndDeletedAt(@Param("keyWord") String keyWord);

    @Query( value = "SELECT a FROM Accounts a WHERE a.deleteAt = false ")
    List<Accounts> findAllAccountCustomer();

    
    Optional<Accounts> findByEmail(String email);

    Optional<Accounts> findByTelephone(String telephone);
}
