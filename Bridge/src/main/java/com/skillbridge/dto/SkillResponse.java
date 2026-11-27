package com.skillbridge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbridge.enums.SkillType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillResponse {

    private Long id;
    private Long skillId;
    private String skillName;
    private SkillType skillType;
    private Long studentId;
    private String studentName;

    public SkillResponse() {
    }

    public SkillResponse(Long id, Long skillId, String skillName, SkillType skillType, Long studentId, String studentName) {
        this.id = id;
        this.skillId = skillId;
        this.skillName = skillName;
        this.skillType = skillType;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
