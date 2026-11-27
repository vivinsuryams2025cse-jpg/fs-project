package com.skillbridge.dto;

import com.skillbridge.enums.SkillType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    @Size(min = 1, max = 100, message = "Skill name must be between 1 and 100 characters")
    private String skillName;

    @NotNull(message = "Skill type (TEACH or LEARN) is required")
    private SkillType skillType;

    public SkillRequest() {
    }

    public SkillRequest(String skillName, SkillType skillType) {
        this.skillName = skillName;
        this.skillType = skillType;
    }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }
}
