package com.skillbridge.repository;

import com.skillbridge.entity.StudentSkill;
import com.skillbridge.enums.SkillType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {

    List<StudentSkill> findByStudentId(Long studentId);

    Page<StudentSkill> findByStudentId(Long studentId, Pageable pageable);

    List<StudentSkill> findByStudentIdAndSkillType(Long studentId, SkillType skillType);

    Optional<StudentSkill> findByIdAndStudentId(Long id, Long studentId);

    boolean existsByStudentIdAndSkillIdAndSkillType(Long studentId, Long skillId, SkillType skillType);

    boolean existsByStudentIdAndSkillSkillNameIgnoreCaseAndSkillType(Long studentId, String skillName, SkillType skillType);

    @Query("""
            SELECT COUNT(ss1) > 0 FROM StudentSkill ss1
            JOIN StudentSkill ss2 ON ss1.skill.id = ss2.skill.id
            WHERE ss1.student.id = :studentAId
              AND ss1.skillType = com.skillbridge.enums.SkillType.TEACH
              AND ss2.student.id = :studentBId
              AND ss2.skillType = com.skillbridge.enums.SkillType.LEARN
            """)
    boolean existsTeachLearnOverlap(@Param("studentAId") Long studentAId, @Param("studentBId") Long studentBId);
}
