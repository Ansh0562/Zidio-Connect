package com.zidioproject.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobListingDTO {
    private Long id;
    private Long postedById;
    private String postedByName;
    private String title;
    private String description;
    private String type;
    private String location;
    private Boolean remote;
    private LocalDate startDate;
    private Integer durationWeeks;
    private Double stipend;
    private LocalDateTime postedAt;
    private LocalDateTime expiryDate;
    private Boolean isActive;

    // Explicit getter methods
    public Long getId() { return id; }
    public Long getPostedById() { return postedById; }
    public String getPostedByName() { return postedByName; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public Boolean getRemote() { return remote; }
    public LocalDate getStartDate() { return startDate; }
    public Integer getDurationWeeks() { return durationWeeks; }
    public Double getStipend() { return stipend; }
    public LocalDateTime getPostedAt() { return postedAt; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public Boolean getIsActive() { return isActive; }

    // Explicit setter methods
    public void setId(Long id) { this.id = id; }
    public void setPostedById(Long postedById) { this.postedById = postedById; }
    public void setPostedByName(String postedByName) { this.postedByName = postedByName; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setLocation(String location) { this.location = location; }
    public void setRemote(Boolean remote) { this.remote = remote; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setDurationWeeks(Integer durationWeeks) { this.durationWeeks = durationWeeks; }
    public void setStipend(Double stipend) { this.stipend = stipend; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 