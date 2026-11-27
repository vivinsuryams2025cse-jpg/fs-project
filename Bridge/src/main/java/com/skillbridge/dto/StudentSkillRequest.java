package com.skillbridge.dto;

import com.skillbridge.enums.SkillType;
import jakarta.validation.constraints.NotNull;

public class StudentSkillRequest {

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Skill type (TEACH or LEARN) is required")
    private SkillType skillType;

    public StudentSkillRequest() {
    }

    public StudentSkillRequest(Long skillId, SkillType skillType) {
        this.skillId = skillId;
        this.skillType = skillType;
    }

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }
}
