package com.spring.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.model.CustomerProfile;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    @Query("Select c from CustomerProfile c where c.deleteAt = false")
    List<CustomerProfile> findByDeleteAtIsTrue();
    
    Optional<CustomerProfile> findByAccountsId (Long id);
    
    List<CustomerProfile> findByDeleteAtIsFalse();
    
    @Query(value = "select * from CountCustomer", nativeQuery = true)
    List<Map<String, Integer>> report();
    
//    @Query(value = "select \n"
//    		+ "(select count(bk.customer_id) from booking bk where bk.customer_id = ctpf.id) as solandat\n"
//    		+ "from customer_profile ctpf;", nativeQuery = true)
//    List<Integer> listCount();
//    @Query(value = "select \n"
//    		+ "ctpf.fullname\n"
//    		+ "from customer_profile ctpf;", nativeQuery = true)
//    List<String> listTen();
    
    
}
