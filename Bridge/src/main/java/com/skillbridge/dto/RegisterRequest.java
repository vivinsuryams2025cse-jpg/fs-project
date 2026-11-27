package com.skillbridge.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 120, message = "Full name must be between 2 and 120 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotBlank(message = "College name is required")
    @Size(max = 180, message = "College name must not exceed 180 characters")
    private String collegeName;

    @NotBlank(message = "Department is required")
    @Size(max = 120, message = "Department must not exceed 120 characters")
    private String department;

    @NotNull(message = "Year of study is required")
    @Min(value = 1, message = "Year of study must be at least 1")
    @Max(value = 8, message = "Year of study must not exceed 8")
    private Integer yearOfStudy;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    public RegisterRequest() {
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
