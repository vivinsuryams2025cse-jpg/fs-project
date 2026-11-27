package com.skillbridge.controller;

import com.skillbridge.dto.ApiResponse;
import com.skillbridge.dto.SkillRequest;
import com.skillbridge.dto.SkillResponse;
import com.skillbridge.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Skills", description = "Manage TEACH and LEARN skills for students")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "Add a TEACH or LEARN skill for the logged-in student")
    @PostMapping
    public ResponseEntity<ApiResponse<SkillResponse>> addSkill(@Valid @RequestBody SkillRequest request) {
        SkillResponse created = skillService.addSkillForCurrentStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Skill added successfully", created));
    }

    @Operation(summary = "Get paginated catalog of all platform skills")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SkillResponse>>> getAllSkills(
            @PageableDefault(size = 20, sort = "skillName", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Skills catalog fetched",
                skillService.getAllCatalogSkills(pageable)
        ));
    }

    @Operation(summary = "Get all TEACH and LEARN skills of the logged-in student")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getMySkills() {
        return ResponseEntity.ok(ApiResponse.success(
                "Your skills fetched",
                skillService.getCurrentStudentSkills()
        ));
    }

    @Operation(summary = "Get student-skill mapping by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponse>> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Skill entry fetched",
                skillService.getStudentSkillById(id)
        ));
    }

    @Operation(summary = "Update an existing student-skill entry")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Skill updated successfully",
                skillService.updateStudentSkill(id, request)
        ));
    }

    @Operation(summary = "Delete an existing student-skill entry")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteStudentSkill(id);
        return ResponseEntity.noContent().build();
    }
}
