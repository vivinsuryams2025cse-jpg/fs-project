package com.skillbridge.controller;

import com.skillbridge.dto.ApiResponse;
import com.skillbridge.dto.ConnectionResponse;
import com.skillbridge.dto.SkillResponse;
import com.skillbridge.dto.StudentRequest;
import com.skillbridge.dto.StudentResponse;
import com.skillbridge.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Students", description = "Student profile, skills, and connections management")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Get current authenticated student profile")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> getCurrentProfile() {
        return ResponseEntity.ok(ApiResponse.success(
                "Current student profile fetched",
                studentService.getCurrentStudentProfile()
        ));
    }

    @Operation(summary = "Update current authenticated student profile")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> updateCurrentProfile(
            @Valid @RequestBody StudentRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Profile updated successfully",
                studentService.updateCurrentStudentProfile(request)
        ));
    }

    @Operation(summary = "Get student profile by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Student profile fetched",
                studentService.getStudentById(id)
        ));
    }

    @Operation(summary = "Get skills of the current authenticated student")
    @GetMapping("/me/skills")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getCurrentStudentSkills() {
        return ResponseEntity.ok(ApiResponse.success(
                "Student skills fetched",
                studentService.getCurrentStudentSkills()
        ));
    }

    @Operation(summary = "Get active connections of the current authenticated student")
    @GetMapping("/me/connections")
    public ResponseEntity<ApiResponse<List<ConnectionResponse>>> getCurrentStudentConnections() {
        return ResponseEntity.ok(ApiResponse.success(
                "Student connections fetched",
                studentService.getCurrentStudentConnections()
        ));
    }
}
