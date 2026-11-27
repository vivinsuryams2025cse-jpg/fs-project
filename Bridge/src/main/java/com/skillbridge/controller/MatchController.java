package com.skillbridge.controller;

import com.skillbridge.dto.ApiResponse;
import com.skillbridge.dto.StudentResponse;
import com.skillbridge.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Skill Matching", description = "Mutual skill matching engine backed by MySQL queries")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @Operation(summary = "Find all mutual skill matches for the logged-in student")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getMutualMatches() {
        return ResponseEntity.ok(ApiResponse.success(
                "Mutual matches fetched successfully",
                matchService.getMutualMatchesForCurrentStudent()
        ));
    }

    @Operation(summary = "Search mutual skill matches with optional filters (skillName, college, department)")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> searchMutualMatches(
            @RequestParam(required = false) String skillName,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String department
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Filtered mutual matches fetched successfully",
                matchService.searchMutualMatches(skillName, college, department)
        ));
    }

    @Operation(summary = "Verify and retrieve mutual match details with a specific student")
    @GetMapping("/{studentId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getMatchByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Mutual match details fetched successfully",
                matchService.getMatchDetailsWithStudent(studentId)
        ));
    }
}
