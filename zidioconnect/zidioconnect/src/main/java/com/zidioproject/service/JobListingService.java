package com.zidioproject.service;

import com.zidioproject.dto.JobListingDTO;
import com.zidioproject.entity.JobListing;
import com.zidioproject.entity.User;
import com.zidioproject.repository.JobListingRepository;
import com.zidioproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobListingService {

    private static final Logger logger = LoggerFactory.getLogger(JobListingService.class);

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private UserRepository userRepository;

    public JobListingDTO createJobListing(JobListingDTO jobListingDTO, Long userId) {
        logger.info("Creating job listing for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        logger.info("Found user: {}", user.getName());

        JobListing jobListing = new JobListing();
        logger.info("Setting job listing properties");
        updateJobListingFromDTO(jobListing, jobListingDTO);
        jobListing.setPostedBy(user);
        logger.info("Job listing prepared, saving to database");

        JobListing savedJobListing = jobListingRepository.save(jobListing);
        logger.info("Job listing saved with ID: {}", savedJobListing.getId());
        
        return convertToDTO(savedJobListing);
    }

    public JobListingDTO updateJobListing(Long id, JobListingDTO jobListingDTO) {
        JobListing jobListing = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job listing not found"));

        updateJobListingFromDTO(jobListing, jobListingDTO);
        JobListing updatedJobListing = jobListingRepository.save(jobListing);
        return convertToDTO(updatedJobListing);
    }

    public void deleteJobListing(Long id) {
        JobListing jobListing = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job listing not found"));
        jobListing.setIsActive(false);
        jobListingRepository.save(jobListing);
    }

    public JobListingDTO getJobListing(Long id) {
        JobListing jobListing = jobListingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job listing not found"));
        return convertToDTO(jobListing);
    }

    public List<JobListingDTO> getAllActiveJobListings() {
        return jobListingRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobListingDTO> getJobListingsByType(String type) {
        return jobListingRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobListingDTO> getJobListingsByLocation(String location) {
        return jobListingRepository.findByLocation(location).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobListingDTO> getRemoteJobs() {
        return jobListingRepository.findByRemoteTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void updateJobListingFromDTO(JobListing jobListing, JobListingDTO dto) {
        jobListing.setTitle(dto.getTitle());
        jobListing.setDescription(dto.getDescription());
        jobListing.setType(dto.getType());
        jobListing.setLocation(dto.getLocation());
        jobListing.setRemote(dto.getRemote());
        jobListing.setStartDate(dto.getStartDate());
        jobListing.setDurationWeeks(dto.getDurationWeeks());
        jobListing.setStipend(dto.getStipend());
        jobListing.setExpiryDate(dto.getExpiryDate());
        jobListing.setIsActive(dto.getIsActive());
    }

    private JobListingDTO convertToDTO(JobListing jobListing) {
        JobListingDTO dto = new JobListingDTO();
        dto.setId(jobListing.getId());
        dto.setPostedById(jobListing.getPostedBy() != null ? jobListing.getPostedBy().getId() : null);
        dto.setPostedByName(jobListing.getPostedBy() != null ? jobListing.getPostedBy().getName() : null);
        dto.setTitle(jobListing.getTitle());
        dto.setDescription(jobListing.getDescription());
        dto.setType(jobListing.getType());
        dto.setLocation(jobListing.getLocation());
        dto.setRemote(jobListing.getRemote());
        dto.setStartDate(jobListing.getStartDate());
        dto.setDurationWeeks(jobListing.getDurationWeeks());
        dto.setStipend(jobListing.getStipend());
        dto.setPostedAt(jobListing.getPostedAt());
        dto.setExpiryDate(jobListing.getExpiryDate());
        dto.setIsActive(jobListing.getIsActive());
        return dto;
    }
} 