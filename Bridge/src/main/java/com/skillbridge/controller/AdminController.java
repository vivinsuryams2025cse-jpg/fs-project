package com.skillbridge.controller;

import com.skillbridge.dto.*;
import com.skillbridge.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Management", description = "Administrative endpoints restricted strictly to ADMIN role")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get paginated list of all students")
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getAllStudents(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "All students fetched",
                adminService.getAllStudents(pageable)
        ));
    }

    @Operation(summary = "Get paginated list of all student skill mappings")
    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<Page<SkillResponse>>> getAllSkills(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "All skill mappings fetched",
                adminService.getAllStudentSkills(pageable)
        ));
    }

    @Operation(summary = "Get paginated list of all exchange requests")
    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<Page<ExchangeRequestResponse>>> getAllRequests(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "All exchange requests fetched",
                adminService.getAllExchangeRequests(pageable)
        ));
    }

    @Operation(summary = "Get paginated list of all platform connections")
    @GetMapping("/connections")
    public ResponseEntity<ApiResponse<Page<ConnectionResponse>>> getAllConnections(
            @PageableDefault(size = 20, sort = "connectedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "All connections fetched",
                adminService.getAllConnections(pageable)
        ));
    }

    @Operation(summary = "Get platform analytics dashboard metrics")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(
                "Admin dashboard metrics fetched",
                adminService.getDashboardMetrics()
        ));
    }

    @Operation(summary = "Enable or disable a student account")
    @PatchMapping("/students/{id}/status")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Student account status updated",
                adminService.updateStudentStatus(id, enabled)
        ));
    }
}
