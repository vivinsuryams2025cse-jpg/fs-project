package com.skillbridge.service;

import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.dto.StudentResponse;
import com.skillbridge.entity.Student;
import com.skillbridge.enums.UserRole;
import com.skillbridge.exception.DuplicateResourceException;
import com.skillbridge.repository.StudentRepository;
import com.skillbridge.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock private StudentRepository studentRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Jane Doe");
        req.setEmail("jane@example.com");
        req.setPassword("Secret@123");
        req.setCollegeName("Stanford");
        req.setDepartment("CS");
        req.setYearOfStudy(2);

        when(studentRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret@123")).thenReturn("$2a$10$encodedHash");
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        StudentResponse res = authService.register(req);

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("jane@example.com", res.getEmail());
        assertEquals(UserRole.STUDENT, res.getRole());
        verify(passwordEncoder).encode("Secret@123");
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("dup@example.com");

        when(studentRepository.existsByEmailIgnoreCase("dup@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(req));
        verify(studentRepository, never()).save(any());
    }
}
