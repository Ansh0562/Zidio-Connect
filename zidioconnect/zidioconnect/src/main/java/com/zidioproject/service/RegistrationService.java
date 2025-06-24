package com.zidioproject.service;
import com.zidioproject.dto.RegisterRequest;
import com.zidioproject.entity.*;
import com.zidioproject.repository.AdminProfileRepository;
import com.zidioproject.repository.RecruiterProfileRepository;
import com.zidioproject.repository.StudentProfileRepository;
import com.zidioproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private AdminProfileRepository adminProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        logger.info("Starting registration for user: {}", registerRequest.getEmail());

        try {
            // Step 1: Create and save the User
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setUsername(registerRequest.getEmail()); // Set username same as email
            user.setRole(registerRequest.getRole());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user = userRepository.save(user);
            logger.info("User saved with ID: {}", user.getId());

            // Step 2: Handle role-specific profile creation
            switch (registerRequest.getRole()) {
                case STUDENT:
                    createStudentProfile(user, registerRequest);
                    break;
                case RECRUITER:
                    createRecruiterProfile(user, registerRequest);
                    break;
                case ADMIN:
                    createAdminProfile(user, registerRequest);
                    break;
                default:
                    logger.error("Invalid role specified: {}", registerRequest.getRole());
                    throw new IllegalArgumentException("Invalid role specified");
            }
        } catch (Exception e) {
            logger.error("Error during registration process", e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    private void createStudentProfile(User user, RegisterRequest request) {
        logger.info("Creating student profile for user: {}", user.getId());
        StudentProfile student = new StudentProfile();
        student.setUser(user);
        student.setEducation(request.getEducation());
        student.setSkills(request.getSkills());
        studentProfileRepository.save(student);
        logger.info("Student profile created successfully");
    }

    private void createRecruiterProfile(User user, RegisterRequest request) {
        logger.info("Creating recruiter profile for user: {}", user.getId());
        RecruiterProfile recruiter = new RecruiterProfile();
        recruiter.setUser(user);
        recruiter.setCompanyName(request.getCompanyName());
        recruiter.setDesignation(request.getDesignation());
        recruiterProfileRepository.save(recruiter);
        logger.info("Recruiter profile created successfully");
    }

    private void createAdminProfile(User user, RegisterRequest request) {
        logger.info("Creating admin profile for user: {}", user.getId());
        Adminprofile admin = new Adminprofile();
        admin.setUser(user);
        admin.setDepartment(request.getDepartment());
        adminProfileRepository.save(admin);
        logger.info("Admin profile created successfully");
    }
}
