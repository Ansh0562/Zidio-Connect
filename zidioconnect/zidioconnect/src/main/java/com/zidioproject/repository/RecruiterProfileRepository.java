package com.zidioproject.repository;

import com.zidioproject.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Long> {
    Optional<RecruiterProfile> findByCompanyName(String companyName);
    Optional<RecruiterProfile> findByUserId(Long userId);
}
