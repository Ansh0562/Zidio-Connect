package com.zidioproject.dto;

import com.zidioproject.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private String education;
    private String skills;
    private String companyName;
    private String designation;
    private String department;

    public String getEducation() { return education; }
    public String getSkills() { return skills; }
    public String getCompanyName() { return companyName; }
    public String getDesignation() { return designation; }
    public String getDepartment() { return department; }
}
