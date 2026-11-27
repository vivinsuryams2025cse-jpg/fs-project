package com.skillbridge.service;

import com.skillbridge.dto.ConnectionResponse;
import com.skillbridge.dto.SkillResponse;
import com.skillbridge.dto.StudentRequest;
import com.skillbridge.dto.StudentResponse;
import com.skillbridge.entity.Connection;
import com.skillbridge.entity.Student;
import com.skillbridge.entity.StudentSkill;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.ConnectionRepository;
import com.skillbridge.repository.StudentRepository;
import com.skillbridge.repository.StudentSkillRepository;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final ConnectionRepository connectionRepository;

    public StudentService(
            StudentRepository studentRepository,
            StudentSkillRepository studentSkillRepository,
            ConnectionRepository connectionRepository
    ) {
        this.studentRepository = studentRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.connectionRepository = connectionRepository;
    }

    @Transactional(readOnly = true)
    public StudentResponse getCurrentStudentProfile() {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return getStudentById(currentStudentId);
    }

    @Transactional
    public StudentResponse updateCurrentStudentProfile(StudentRequest request) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        Student student = studentRepository.findById(currentStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

        student.setFullName(request.getFullName().trim());
        student.setCollegeName(request.getCollegeName().trim());
        student.setDepartment(request.getDepartment().trim());
        student.setYearOfStudy(request.getYearOfStudy());
        student.setBio(request.getBio() != null ? request.getBio().trim() : null);

        Student updated = studentRepository.save(student);
        List<StudentSkill> skills = studentSkillRepository.findByStudentId(currentStudentId);
        return DtoMapper.toStudentResponse(updated, skills);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        List<StudentSkill> skills = studentSkillRepository.findByStudentId(id);
        return DtoMapper.toStudentResponse(student, skills);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getCurrentStudentSkills() {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return studentSkillRepository.findByStudentId(currentStudentId)
                .stream()
                .map(DtoMapper::toSkillResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConnectionResponse> getCurrentStudentConnections() {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        List<Connection> connections = connectionRepository.findAllByStudentId(currentStudentId);
        return connections.stream()
                .map(c -> DtoMapper.toConnectionResponse(c, currentStudentId))
                .toList();
    }
}
