package com.zidioproject.repository;

import com.zidioproject.entity.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    
    // Find all active job listings
    List<JobListing> findByIsActiveTrue();
    
    // Find jobs posted by a specific user
    List<JobListing> findByPostedById(Long postedById);
    
    // Find jobs by type (JOB or INTERNSHIP)
    List<JobListing> findByType(String type);
    
    // Find jobs by location
    List<JobListing> findByLocation(String location);
    
    // Find remote jobs
    List<JobListing> findByRemoteTrue();
    
    // Find jobs by type and location
    List<JobListing> findByTypeAndLocation(String type, String location);
    
    // Find active jobs by type
    List<JobListing> findByIsActiveTrueAndType(String type);
    
    // Search jobs by title (case insensitive)
    List<JobListing> findByTitleContainingIgnoreCase(String title);
    
    // Find jobs by recruiter ID
    List<JobListing> findByPostedBy_Id(Long recruiterId);
}
