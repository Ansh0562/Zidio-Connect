package com.zidioproject.controller;

import com.zidioproject.dto.JobListingDTO;
import com.zidioproject.service.JobListingService;
import com.zidioproject.entity.User;
import com.zidioproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/joblistings")
@CrossOrigin(origins = "*")
public class JobListingController {

    private static final Logger logger = LoggerFactory.getLogger(JobListingController.class);

    @Autowired
    private JobListingService jobListingService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Only users with RECRUITER or ADMIN roles can create job listings.
     */
    @PreAuthorize("hasAnyAuthority('RECRUITER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createJobListing(@RequestBody JobListingDTO jobListingDTO) {
        try {
            logger.info("Received job listing request: {}", jobListingDTO);
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("User not authenticated");
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String username = authentication.getName();
            logger.info("Authenticated user: {}", username);
            
            // Find the user by username or email
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElse(null));
            
            if (user == null) {
                logger.warn("User not found for username/email: {}", username);
                return ResponseEntity.status(404).body("User not found");
            }

            logger.info("Using user ID: {}", user.getId());
            
            JobListingDTO created = jobListingService.createJobListing(jobListingDTO, user.getId());
            logger.info("Job listing created successfully");
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            logger.error("RuntimeException while creating job listing: ", e);
            if (e.getMessage().contains("User not found")) {
                return ResponseEntity.badRequest().body("Error: User not found. Please check your authentication.");
            }
            return ResponseEntity.badRequest().body("Error creating job listing: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception while creating job listing: ", e);
            return ResponseEntity.badRequest().body("Error creating job listing: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllActiveJobListings() {
        try {
            List<JobListingDTO> jobs = jobListingService.getAllActiveJobListings();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching job listings: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobListing(@PathVariable Long id) {
        try {
            JobListingDTO job = jobListingService.getJobListing(id);
            return ResponseEntity.ok(job);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching job listing: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJobListing(@PathVariable Long id, @RequestBody JobListingDTO jobListingDTO) {
        try {
            JobListingDTO updated = jobListingService.updateJobListing(id, jobListingDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating job listing: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJobListing(@PathVariable Long id) {
        try {
            jobListingService.deleteJobListing(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting job listing: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getJobListingsByType(@PathVariable String type) {
        try {
            List<JobListingDTO> jobs = jobListingService.getJobListingsByType(type);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching jobs by type: " + e.getMessage());
        }
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<?> getJobListingsByLocation(@PathVariable String location) {
        try {
            List<JobListingDTO> jobs = jobListingService.getJobListingsByLocation(location);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching jobs by location: " + e.getMessage());
        }
    }

    @GetMapping("/remote")
    public ResponseEntity<?> getRemoteJobs() {
        try {
            List<JobListingDTO> jobs = jobListingService.getRemoteJobs();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching remote jobs: " + e.getMessage());
        }
    }
} 