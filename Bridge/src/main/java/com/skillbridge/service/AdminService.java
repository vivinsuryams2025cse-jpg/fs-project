package com.skillbridge.service;

import com.skillbridge.dto.*;
import com.skillbridge.entity.Student;
import com.skillbridge.entity.StudentSkill;
import com.skillbridge.enums.RequestStatus;
import com.skillbridge.enums.UserRole;
import com.skillbridge.exception.BadRequestException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.*;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ConnectionRepository connectionRepository;

    public AdminService(
            StudentRepository studentRepository,
            SkillRepository skillRepository,
            StudentSkillRepository studentSkillRepository,
            ExchangeRequestRepository exchangeRequestRepository,
            ConnectionRepository connectionRepository
    ) {
        this.studentRepository = studentRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.connectionRepository = connectionRepository;
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(student -> {
                    List<StudentSkill> skills = studentSkillRepository.findByStudentId(student.getId());
                    return DtoMapper.toStudentResponse(student, skills);
                });
    }

    @Transactional(readOnly = true)
    public Page<SkillResponse> getAllStudentSkills(Pageable pageable) {
        return studentSkillRepository.findAll(pageable)
                .map(DtoMapper::toSkillResponse);
    }

    @Transactional(readOnly = true)
    public Page<ExchangeRequestResponse> getAllExchangeRequests(Pageable pageable) {
        return exchangeRequestRepository.findAll(pageable)
                .map(DtoMapper::toExchangeRequestResponse);
    }

    @Transactional(readOnly = true)
    public Page<ConnectionResponse> getAllConnections(Pageable pageable) {
        return connectionRepository.findAll(pageable)
                .map(c -> DtoMapper.toConnectionResponse(c, null));
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardMetrics() {
        AdminDashboardResponse metrics = new AdminDashboardResponse();
        metrics.setTotalStudents(studentRepository.countByRole(UserRole.STUDENT));
        metrics.setActiveStudents(studentRepository.countByEnabledTrue());
        metrics.setTotalUniqueSkills(skillRepository.count());
        metrics.setTotalStudentSkillMappings(studentSkillRepository.count());
        metrics.setTotalExchangeRequests(exchangeRequestRepository.count());
        metrics.setPendingExchangeRequests(exchangeRequestRepository.countByStatus(RequestStatus.PENDING));
        metrics.setAcceptedExchangeRequests(exchangeRequestRepository.countByStatus(RequestStatus.ACCEPTED));
        metrics.setRejectedExchangeRequests(exchangeRequestRepository.countByStatus(RequestStatus.REJECTED));
        metrics.setTotalConnections(connectionRepository.count());
        return metrics;
    }

    @Transactional
    public StudentResponse updateStudentStatus(Long studentId, boolean enabled) {
        Long currentAdminId = SecurityUtils.getCurrentStudentId();
        if (currentAdminId.equals(studentId)) {
            throw new BadRequestException("Administrators cannot disable their own account");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        student.setEnabled(enabled);
        Student saved = studentRepository.save(student);
        List<StudentSkill> skills = studentSkillRepository.findByStudentId(studentId);
        return DtoMapper.toStudentResponse(saved, skills);
    }
}
