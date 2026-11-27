package com.skillbridge.service;

import com.skillbridge.dto.StudentResponse;
import com.skillbridge.entity.Student;
import com.skillbridge.entity.StudentSkill;
import com.skillbridge.enums.SkillType;
import com.skillbridge.enums.UserRole;
import com.skillbridge.exception.BadRequestException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.ConnectionRepository;
import com.skillbridge.repository.StudentRepository;
import com.skillbridge.repository.StudentSkillRepository;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final StudentRepository studentRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final ConnectionRepository connectionRepository;

    public MatchService(
            StudentRepository studentRepository,
            StudentSkillRepository studentSkillRepository,
            ConnectionRepository connectionRepository
    ) {
        this.studentRepository = studentRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.connectionRepository = connectionRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getMutualMatchesForCurrentStudent() {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        List<StudentSkill> mySkills = studentSkillRepository.findByStudentId(currentStudentId);
        List<Student> matchedStudents = studentRepository.findMutualMatches(currentStudentId);

        return matchedStudents.stream()
                .distinct()
                .map(candidate -> enrichMatchResponse(candidate, mySkills))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> searchMutualMatches(String skillName, String college, String department) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        List<StudentSkill> mySkills = studentSkillRepository.findByStudentId(currentStudentId);
        List<Student> matchedStudents = studentRepository.searchMutualMatches(
                currentStudentId, skillName, college, department
        );

        return matchedStudents.stream()
                .distinct()
                .map(candidate -> enrichMatchResponse(candidate, mySkills))
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getMatchDetailsWithStudent(Long targetStudentId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();

        if (currentStudentId.equals(targetStudentId)) {
            throw new BadRequestException("Cannot evaluate mutual skill match with yourself");
        }

        Student candidate = studentRepository.findById(targetStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + targetStudentId));

        if (!candidate.isEnabled() || candidate.getRole() != UserRole.STUDENT) {
            throw new ResourceNotFoundException("Active student profile not found with ID: " + targetStudentId);
        }

        if (connectionRepository.existsBetweenStudents(currentStudentId, targetStudentId)) {
            throw new BadRequestException("You are already connected with this student");
        }

        if (!isMutualMatch(currentStudentId, targetStudentId)) {
            throw new BadRequestException("No mutual skill exchange match exists between you and student ID: " + targetStudentId);
        }

        List<StudentSkill> mySkills = studentSkillRepository.findByStudentId(currentStudentId);
        return enrichMatchResponse(candidate, mySkills);
    }

    @Transactional(readOnly = true)
    public boolean isMutualMatch(Long studentAId, Long studentBId) {
        boolean aTeachesWhatBLearns = studentSkillRepository.existsTeachLearnOverlap(studentAId, studentBId);
        boolean bTeachesWhatALearns = studentSkillRepository.existsTeachLearnOverlap(studentBId, studentAId);
        return aTeachesWhatBLearns && bTeachesWhatALearns;
    }

    private StudentResponse enrichMatchResponse(Student candidate, List<StudentSkill> mySkills) {
        List<StudentSkill> candidateSkills = studentSkillRepository.findByStudentId(candidate.getId());
        StudentResponse response = DtoMapper.toStudentResponse(candidate, candidateSkills);

        Set<String> myTeachLower = mySkills.stream()
                .filter(s -> s.getSkillType() == SkillType.TEACH)
                .map(s -> s.getSkill().getSkillName().toLowerCase())
                .collect(Collectors.toSet());

        Set<String> myLearnLower = mySkills.stream()
                .filter(s -> s.getSkillType() == SkillType.LEARN)
                .map(s -> s.getSkill().getSkillName().toLowerCase())
                .collect(Collectors.toSet());

        List<String> canLearnFromCandidate = candidateSkills.stream()
                .filter(s -> s.getSkillType() == SkillType.TEACH)
                .map(s -> s.getSkill().getSkillName())
                .filter(name -> myLearnLower.contains(name.toLowerCase()))
                .distinct()
                .toList();

        List<String> canTeachCandidate = candidateSkills.stream()
                .filter(s -> s.getSkillType() == SkillType.LEARN)
                .map(s -> s.getSkill().getSkillName())
                .filter(name -> myTeachLower.contains(name.toLowerCase()))
                .distinct()
                .toList();

        response.setMatchingSkillsYouCanLearn(canLearnFromCandidate);
        response.setMatchingSkillsYouCanTeach(canTeachCandidate);
        return response;
    }
}
