package com.spring.repository;

import com.spring.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByTokenAndCerifiedIsNull(String token);

    @Query(value = "SELECT v FROM VerificationToken  v WHERE v.accounts.id = :id AND v.cerified IS NULL AND v.type = 'VERIFY_ACCOUNT' ")
    Optional<VerificationToken> findByAccountIdAndCerifiedIsNull(@Param("id") Long id);
}
