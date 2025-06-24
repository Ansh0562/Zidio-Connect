package com.zidioproject.controller;

import com.zidioproject.entity.StudentProfile;
import com.zidioproject.entity.User;
import com.zidioproject.repository.StudentProfileRepository;
import com.zidioproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(@RequestBody StudentProfile profileRequest) {
        try {
            logger.info("Creating student profile");
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            // Check if profile already exists
            if (studentProfileRepository.findByUserId(user.getId()).isPresent()) {
                return ResponseEntity.badRequest().body("Profile already exists for this user");
            }

            // Create new profile
            StudentProfile profile = new StudentProfile();
            profile.setUser(user);
            profile.setEducation(profileRequest.getEducation());
            profile.setSkills(profileRequest.getSkills());
            profile.setResumeUrl(profileRequest.getResumeUrl());

            StudentProfile savedProfile = studentProfileRepository.save(profile);
            logger.info("Student profile created successfully");
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            logger.error("Error creating student profile: ", e);
            return ResponseEntity.badRequest().body("Error creating profile: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            logger.info("Getting student profile");
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            StudentProfile profile = studentProfileRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            logger.info("Student profile retrieved successfully");
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error getting student profile: ", e);
            return ResponseEntity.badRequest().body("Error getting profile: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody StudentProfile profileRequest) {
        try {
            logger.info("Updating student profile");
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            StudentProfile profile = studentProfileRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            // Update fields
            if (profileRequest.getEducation() != null) profile.setEducation(profileRequest.getEducation());
            if (profileRequest.getSkills() != null) profile.setSkills(profileRequest.getSkills());
            if (profileRequest.getResumeUrl() != null) profile.setResumeUrl(profileRequest.getResumeUrl());

            StudentProfile updatedProfile = studentProfileRepository.save(profile);
            logger.info("Student profile updated successfully");
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            logger.error("Error updating student profile: ", e);
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }
} 