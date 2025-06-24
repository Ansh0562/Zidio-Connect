package com.zidioproject.controller;

import com.zidioproject.dto.RecruiterDto;
import com.zidioproject.entity.RecruiterProfile;
import com.zidioproject.entity.User;
import com.zidioproject.repository.RecruiterProfileRepository;
import com.zidioproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin(origins = "*")
public class RecruiterController {

    private static final Logger logger = LoggerFactory.getLogger(RecruiterController.class);

    @Autowired
    private RecruiterProfileRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Save or update recruiter profile
     * Only users with RECRUITER role can access this endpoint
     */
    @PreAuthorize("hasAuthority('RECRUITER')")
    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(@RequestBody RecruiterDto profileRequest) {
        try {
            logger.info("Creating recruiter profile");
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            // Check if profile already exists
            if (recruiterRepository.findByUserId(user.getId()).isPresent()) {
                return ResponseEntity.badRequest().body("Profile already exists for this user");
            }

            // Create new profile
            RecruiterProfile profile = new RecruiterProfile();
            profile.setUser(user);
            profile.setCompanyName(profileRequest.getCompanyName());
            profile.setDesignation(profileRequest.getDesignation());

            RecruiterProfile savedProfile = recruiterRepository.save(profile);
            logger.info("Recruiter profile created successfully");
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            logger.error("Error creating recruiter profile: ", e);
            return ResponseEntity.badRequest().body("Error creating profile: " + e.getMessage());
        }
    }

    /**
     * Get current recruiter's profile
     * Only users with RECRUITER role can access this endpoint
     */
    @PreAuthorize("hasAuthority('RECRUITER')")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            logger.info("Getting recruiter profile");
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            RecruiterProfile profile = recruiterRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            logger.info("Recruiter profile retrieved successfully");
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error getting recruiter profile: ", e);
            return ResponseEntity.badRequest().body("Error getting profile: " + e.getMessage());
        }
    }

    /**
     * Get all recruiters (Admin only)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllRecruiters() {
        try {
            logger.info("Getting all recruiters");
            List<RecruiterProfile> recruiters = recruiterRepository.findAll();
            logger.info("Retrieved {} recruiters", recruiters.size());
            return ResponseEntity.ok(recruiters);
        } catch (Exception e) {
            logger.error("Error getting all recruiters: ", e);
            return ResponseEntity.badRequest().body("Error getting recruiters: " + e.getMessage());
        }
    }
} 