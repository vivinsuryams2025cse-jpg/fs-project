package com.skillbridge.entity;

import com.skillbridge.enums.SkillType;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "student_skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_skill_type",
                        columnNames = {"student_id", "skill_id", "skill_type"}
                )
        },
        indexes = {
                @Index(name = "idx_student_skills_student", columnList = "student_id"),
                @Index(name = "idx_student_skills_skill", columnList = "skill_id"),
                @Index(name = "idx_student_skills_type", columnList = "skill_type")
        }
)
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_student_skills_student"))
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "skill_id", nullable = false, foreignKey = @ForeignKey(name = "fk_student_skills_skill"))
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type", nullable = false, length = 15)
    private SkillType skillType;

    public StudentSkill() {
    }

    public StudentSkill(Student student, Skill skill, SkillType skillType) {
        this.student = student;
        this.skill = skill;
        this.skillType = skillType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentSkill that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
