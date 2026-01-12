package com.example.hcms.member.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Request DTO for creating a Member
 */
public class CreateMemberRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Long departmentId;
    private String jobTitle;
    private String alias;
    private String deskId;
    private String phoneExtension;
    private String employeeNumber;
    private String gender;
    private String workforceType;
    private LocalDate dateOfEmployment;
    private String country;
    private String city;
    private String directManager;
    private String dottedLineManager;
    private Long attendanceGroupId;

    // Constructors
    public CreateMemberRequest() {
    }

    public CreateMemberRequest(Long userId) {
        this.userId = userId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWorkforceType() {
        return workforceType;
    }

    public void setWorkforceType(String workforceType) {
        this.workforceType = workforceType;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDirectManager() {
        return directManager;
    }

    public void setDirectManager(String directManager) {
        this.directManager = directManager;
    }

    public String getDottedLineManager() {
        return dottedLineManager;
    }

    public void setDottedLineManager(String dottedLineManager) {
        this.dottedLineManager = dottedLineManager;
    }

    public Long getAttendanceGroupId() {
        return attendanceGroupId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }
}
