package com.zidioproject.repository;

import com.zidioproject.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    List<Application> findByStudent_Id(Long studentId);
    
    List<Application> findByJobListing_Id(Long jobListingId);
    
    List<Application> findByStatus(String status);
    
    List<Application> findByStudent_IdAndStatus(Long studentId, String status);
    
    List<Application> findByJobListing_IdAndStatus(Long jobListingId, String status);
} 