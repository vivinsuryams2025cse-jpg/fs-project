package com.skillbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "skills",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_skills_name", columnNames = "skill_name")
        },
        indexes = {
                @Index(name = "idx_skills_name", columnList = "skill_name")
        }
)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name", nullable = false, unique = true, length = 100)
    private String skillName;

    @JsonIgnore
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentSkill> studentSkills = new ArrayList<>();

    public Skill() {
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public List<StudentSkill> getStudentSkills() { return studentSkills; }
    public void setStudentSkills(List<StudentSkill> studentSkills) { this.studentSkills = studentSkills; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill skill)) return false;
        return Objects.equals(id, skill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
