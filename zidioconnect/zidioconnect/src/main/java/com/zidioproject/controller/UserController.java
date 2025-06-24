package com.zidioproject.controller;

import com.zidioproject.entity.User;
import com.zidioproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            logger.info("User profile request received");
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Authentication object: {}", authentication);
            
            if (authentication == null) {
                logger.warn("Authentication is null");
                return ResponseEntity.status(401).body("User not authenticated");
            }
            
            if (!authentication.isAuthenticated()) {
                logger.warn("User is not authenticated");
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String username = authentication.getName();
            logger.info("Username from authentication: {}", username);
            
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElse(null));
            
            if (user == null) {
                logger.warn("User not found for username/email: {}", username);
                return ResponseEntity.status(404).body("User not found");
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().name());

            logger.info("User profile returned successfully for user: {}", username);
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            logger.error("Error fetching user profile: ", e);
            return ResponseEntity.status(500).body("Error fetching user profile: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<Map<String, Object>> userList = users.stream()
                    .map(user -> {
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", user.getId());
                        userData.put("name", user.getName());
                        userData.put("email", user.getEmail());
                        userData.put("role", user.getRole().name());
                        return userData;
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            logger.error("Error fetching users: ", e);
            return ResponseEntity.status(500).body("Error fetching users: " + e.getMessage());
        }
    }

    @GetMapping("/recruiters")
    public ResponseEntity<?> getRecruiters() {
        try {
            List<User> recruiters = userRepository.findByRole(com.zidioproject.entity.Role.RECRUITER);
            List<Map<String, Object>> recruiterList = recruiters.stream()
                    .map(user -> {
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", user.getId());
                        userData.put("name", user.getName());
                        userData.put("email", user.getEmail());
                        return userData;
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(recruiterList);
        } catch (Exception e) {
            logger.error("Error fetching recruiters: ", e);
            return ResponseEntity.status(500).body("Error fetching recruiters: " + e.getMessage());
        }
    }
} 