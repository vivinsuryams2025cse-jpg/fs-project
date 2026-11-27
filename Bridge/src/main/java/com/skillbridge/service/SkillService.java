package com.skillbridge.service;

import com.skillbridge.dto.SkillRequest;
import com.skillbridge.dto.SkillResponse;
import com.skillbridge.entity.Skill;
import com.skillbridge.entity.Student;
import com.skillbridge.entity.StudentSkill;
import com.skillbridge.exception.DuplicateResourceException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.SkillRepository;
import com.skillbridge.repository.StudentRepository;
import com.skillbridge.repository.StudentSkillRepository;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final StudentRepository studentRepository;

    public SkillService(
            SkillRepository skillRepository,
            StudentSkillRepository studentSkillRepository,
            StudentRepository studentRepository
    ) {
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public SkillResponse addSkillForCurrentStudent(SkillRequest request) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        Student student = studentRepository.findById(currentStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated student not found"));

        String normalizedSkillName = normalizeSkillName(request.getSkillName());

        if (studentSkillRepository.existsByStudentIdAndSkillSkillNameIgnoreCaseAndSkillType(
                currentStudentId, normalizedSkillName, request.getSkillType())) {
            throw new DuplicateResourceException(
                    "Skill '" + normalizedSkillName + "' is already added as " + request.getSkillType()
            );
        }

        Skill skill = skillRepository.findBySkillNameIgnoreCase(normalizedSkillName)
                .orElseGet(() -> skillRepository.save(new Skill(normalizedSkillName)));

        StudentSkill studentSkill = new StudentSkill(student, skill, request.getSkillType());
        StudentSkill saved = studentSkillRepository.save(studentSkill);

        return DtoMapper.toSkillResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<SkillResponse> getAllCatalogSkills(Pageable pageable) {
        return skillRepository.findAll(pageable)
                .map(DtoMapper::toCatalogSkillResponse);
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
    public SkillResponse getStudentSkillById(Long id) {
        StudentSkill studentSkill = studentSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill entry not found with ID: " + id));
        return DtoMapper.toSkillResponse(studentSkill);
    }

    @Transactional
    public SkillResponse updateStudentSkill(Long id, SkillRequest request) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        StudentSkill existing = studentSkillRepository.findByIdAndStudentId(id, currentStudentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Skill entry not found with ID: " + id + " for current student"
                ));

        String normalizedSkillName = normalizeSkillName(request.getSkillName());

        boolean isSameSkillAndType = existing.getSkill().getSkillName().equalsIgnoreCase(normalizedSkillName)
                && existing.getSkillType() == request.getSkillType();

        if (!isSameSkillAndType && studentSkillRepository.existsByStudentIdAndSkillSkillNameIgnoreCaseAndSkillType(
                currentStudentId, normalizedSkillName, request.getSkillType())) {
            throw new DuplicateResourceException(
                    "Skill '" + normalizedSkillName + "' already exists as " + request.getSkillType()
            );
        }

        Skill skill = skillRepository.findBySkillNameIgnoreCase(normalizedSkillName)
                .orElseGet(() -> skillRepository.save(new Skill(normalizedSkillName)));

        existing.setSkill(skill);
        existing.setSkillType(request.getSkillType());

        StudentSkill updated = studentSkillRepository.save(existing);
        return DtoMapper.toSkillResponse(updated);
    }

    @Transactional
    public void deleteStudentSkill(Long id) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        StudentSkill existing = studentSkillRepository.findByIdAndStudentId(id, currentStudentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Skill entry not found with ID: " + id + " for current student"
                ));
        studentSkillRepository.delete(existing);
    }

    private String normalizeSkillName(String rawName) {
        return rawName.trim().replaceAll("\\s+", " ");
    }
}
