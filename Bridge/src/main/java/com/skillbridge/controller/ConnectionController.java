package com.skillbridge.controller;

import com.skillbridge.dto.ApiResponse;
import com.skillbridge.dto.ConnectionResponse;
import com.skillbridge.service.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/connections")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Connections", description = "View and manage established skill exchange connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Operation(summary = "Get paginated connections for the logged-in student")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ConnectionResponse>>> getMyConnections(
            @PageableDefault(size = 20, sort = "connectedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Connections fetched successfully",
                connectionService.getMyConnections(pageable)
        ));
    }

    @Operation(summary = "Get connection details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConnectionResponse>> getConnectionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Connection details fetched",
                connectionService.getConnectionById(id)
        ));
    }

    @Operation(summary = "Remove/disconnect an existing connection")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }
}
