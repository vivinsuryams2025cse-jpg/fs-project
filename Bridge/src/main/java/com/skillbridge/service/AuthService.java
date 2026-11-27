package com.skillbridge.service;

import com.skillbridge.dto.LoginRequest;
import com.skillbridge.dto.LoginResponse;
import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.dto.StudentResponse;
import com.skillbridge.entity.Student;
import com.skillbridge.enums.UserRole;
import com.skillbridge.exception.DuplicateResourceException;
import com.skillbridge.repository.StudentRepository;
import com.skillbridge.security.CustomUserDetails;
import com.skillbridge.security.JwtService;
import com.skillbridge.util.DtoMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public StudentResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (studentRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateResourceException("Email is already registered: " + normalizedEmail);
        }

        Student student = new Student();
        student.setFullName(request.getFullName().trim());
        student.setEmail(normalizedEmail);
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setCollegeName(request.getCollegeName().trim());
        student.setDepartment(request.getDepartment().trim());
        student.setYearOfStudy(request.getYearOfStudy());
        student.setBio(request.getBio() != null ? request.getBio().trim() : null);
        student.setRole(UserRole.STUDENT);
        student.setEnabled(true);

        Student savedStudent = studentRepository.save(student);
        return DtoMapper.toStudentResponse(savedStudent, List.of());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                jwtService.getJwtExpirationMs(),
                userDetails.getId(),
                userDetails.getFullName(),
                userDetails.getUsername(),
                userDetails.getRole()
        );
    }

    public void logout(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            jwtService.invalidateToken(token);
        }
        SecurityContextHolder.clearContext();
    }
}
