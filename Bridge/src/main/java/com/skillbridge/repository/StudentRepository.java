package com.skillbridge.repository;

import com.skillbridge.entity.Student;
import com.skillbridge.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    long countByRole(UserRole role);

    long countByEnabledTrue();

    Page<Student> findByRole(UserRole role, Pageable pageable);

    @Query("""
            SELECT DISTINCT s FROM Student s
            WHERE s.id <> :currentStudentId
              AND s.enabled = true
              AND s.role = com.skillbridge.enums.UserRole.STUDENT
              AND EXISTS (
                  SELECT 1 FROM StudentSkill ssTeach
                  JOIN StudentSkill myLearn ON LOWER(ssTeach.skill.skillName) = LOWER(myLearn.skill.skillName)
                  WHERE ssTeach.student.id = s.id
                    AND ssTeach.skillType = com.skillbridge.enums.SkillType.TEACH
                    AND myLearn.student.id = :currentStudentId
                    AND myLearn.skillType = com.skillbridge.enums.SkillType.LEARN
              )
              AND EXISTS (
                  SELECT 1 FROM StudentSkill ssLearn
                  JOIN StudentSkill myTeach ON LOWER(ssLearn.skill.skillName) = LOWER(myTeach.skill.skillName)
                  WHERE ssLearn.student.id = s.id
                    AND ssLearn.skillType = com.skillbridge.enums.SkillType.LEARN
                    AND myTeach.student.id = :currentStudentId
                    AND myTeach.skillType = com.skillbridge.enums.SkillType.TEACH
              )
              AND NOT EXISTS (
                  SELECT 1 FROM Connection c
                  WHERE (c.studentOne.id = :currentStudentId AND c.studentTwo.id = s.id)
                     OR (c.studentTwo.id = :currentStudentId AND c.studentOne.id = s.id)
              )
            """)
    List<Student> findMutualMatches(@Param("currentStudentId") Long currentStudentId);

    @Query("""
            SELECT DISTINCT s FROM Student s
            WHERE s.id <> :currentStudentId
              AND s.enabled = true
              AND s.role = com.skillbridge.enums.UserRole.STUDENT
              AND EXISTS (
                  SELECT 1 FROM StudentSkill ssTeach
                  JOIN StudentSkill myLearn ON LOWER(ssTeach.skill.skillName) = LOWER(myLearn.skill.skillName)
                  WHERE ssTeach.student.id = s.id
                    AND ssTeach.skillType = com.skillbridge.enums.SkillType.TEACH
                    AND myLearn.student.id = :currentStudentId
                    AND myLearn.skillType = com.skillbridge.enums.SkillType.LEARN
              )
              AND EXISTS (
                  SELECT 1 FROM StudentSkill ssLearn
                  JOIN StudentSkill myTeach ON LOWER(ssLearn.skill.skillName) = LOWER(myTeach.skill.skillName)
                  WHERE ssLearn.student.id = s.id
                    AND ssLearn.skillType = com.skillbridge.enums.SkillType.LEARN
                    AND myTeach.student.id = :currentStudentId
                    AND myTeach.skillType = com.skillbridge.enums.SkillType.TEACH
              )
              AND NOT EXISTS (
                  SELECT 1 FROM Connection c
                  WHERE (c.studentOne.id = :currentStudentId AND c.studentTwo.id = s.id)
                     OR (c.studentTwo.id = :currentStudentId AND c.studentOne.id = s.id)
              )
              AND (:skillName IS NULL OR :skillName = '' OR EXISTS (
                  SELECT 1 FROM StudentSkill sk
                  WHERE sk.student.id = s.id
                    AND LOWER(sk.skill.skillName) LIKE LOWER(CONCAT('%', :skillName, '%'))
              ))
              AND (:college IS NULL OR :college = '' OR LOWER(s.collegeName) LIKE LOWER(CONCAT('%', :college, '%')))
              AND (:department IS NULL OR :department = '' OR LOWER(s.department) LIKE LOWER(CONCAT('%', :department, '%')))
            """)
    List<Student> searchMutualMatches(
            @Param("currentStudentId") Long currentStudentId,
            @Param("skillName") String skillName,
            @Param("college") String college,
            @Param("department") String department
    );
}
