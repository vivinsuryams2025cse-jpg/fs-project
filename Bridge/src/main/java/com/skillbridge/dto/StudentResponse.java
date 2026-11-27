package com.skillbridge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbridge.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {

    private Long id;
    private String fullName;
    private String email;
    private String collegeName;
    private String department;
    private Integer yearOfStudy;
    private String bio;
    private UserRole role;
    private boolean enabled;
    private LocalDateTime createdAt;
    private List<SkillResponse> teachSkills;
    private List<SkillResponse> learnSkills;
    private List<String> matchingSkillsYouCanLearn;
    private List<String> matchingSkillsYouCanTeach;

    public StudentResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<SkillResponse> getTeachSkills() { return teachSkills; }
    public void setTeachSkills(List<SkillResponse> teachSkills) { this.teachSkills = teachSkills; }

    public List<SkillResponse> getLearnSkills() { return learnSkills; }
    public void setLearnSkills(List<SkillResponse> learnSkills) { this.learnSkills = learnSkills; }

    public List<String> getMatchingSkillsYouCanLearn() { return matchingSkillsYouCanLearn; }
    public void setMatchingSkillsYouCanLearn(List<String> matchingSkillsYouCanLearn) { this.matchingSkillsYouCanLearn = matchingSkillsYouCanLearn; }

    public List<String> getMatchingSkillsYouCanTeach() { return matchingSkillsYouCanTeach; }
    public void setMatchingSkillsYouCanTeach(List<String> matchingSkillsYouCanTeach) { this.matchingSkillsYouCanTeach = matchingSkillsYouCanTeach; }
}
