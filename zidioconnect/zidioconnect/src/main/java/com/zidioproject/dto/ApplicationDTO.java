package com.zidioproject.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicationDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long jobListingId;
    private String jobTitle;
    private String status;
    private LocalDate appliedAt;
} 