package com.zidioproject.controller;

import com.zidioproject.entity.Adminprofile;
import com.zidioproject.entity.User;
import com.zidioproject.repository.AdminProfileRepository;
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
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminProfileRepository adminProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(@RequestBody Adminprofile profileRequest) {
        try {
            logger.info("Creating admin profile");
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            // Check if profile already exists
            if (adminProfileRepository.findByUser_Email(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Profile already exists for this user");
            }

            // Create new profile
            Adminprofile profile = new Adminprofile();
            profile.setUser(user);
            profile.setDepartment(profileRequest.getDepartment());

            Adminprofile savedProfile = adminProfileRepository.save(profile);
            logger.info("Admin profile created successfully");
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            logger.error("Error creating admin profile: ", e);
            return ResponseEntity.badRequest().body("Error creating profile: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            logger.info("Getting admin profile");
            
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found")));

            Adminprofile profile = adminProfileRepository.findByUser_Email(user.getEmail())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            logger.info("Admin profile retrieved successfully");
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error getting admin profile: ", e);
            return ResponseEntity.badRequest().body("Error getting profile: " + e.getMessage());
        }
    }
} 