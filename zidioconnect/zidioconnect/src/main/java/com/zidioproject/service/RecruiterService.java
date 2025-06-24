package com.zidioproject.service;

import com.zidioproject.dto.RecruiterDto;
import com.zidioproject.entity.RecruiterProfile;
import com.zidioproject.entity.User;
import com.zidioproject.repository.RecruiterProfileRepository;
import com.zidioproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterProfileRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    public RecruiterProfile saveProfile(RecruiterDto dto) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found")));

        // Check if profile already exists
        Optional<RecruiterProfile> existingProfile = recruiterRepository.findByUserId(user.getId());
        
        RecruiterProfile profile;
        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
        } else {
            profile = new RecruiterProfile();
            profile.setUser(user);
        }

        // Update profile data
        profile.setCompanyName(dto.getCompanyName());
        profile.setDesignation(dto.getDesignation());

        return recruiterRepository.save(profile);
    }

    public RecruiterProfile getProfile() {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found")));

        return recruiterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    public List<RecruiterProfile> getAllRecruiters() {
        return recruiterRepository.findAll();
    }
} 