package com.skillbridge.controller;

import com.skillbridge.dto.ApiResponse;
import com.skillbridge.dto.NotificationResponse;
import com.skillbridge.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notifications", description = "View and mark student notifications as read")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Get paginated notifications for the logged-in student")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getMyNotifications(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Notifications fetched successfully",
                notificationService.getMyNotifications(pageable)
        ));
    }

    @Operation(summary = "Get all unread notifications for the logged-in student")
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications() {
        return ResponseEntity.ok(ApiResponse.success(
                "Unread notifications fetched",
                notificationService.getMyUnreadNotifications()
        ));
    }

    @Operation(summary = "Mark a specific notification as read")
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Notification marked as read",
                notificationService.markAsRead(id)
        ));
    }

    @Operation(summary = "Mark all unread notifications as read")
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> markAllAsRead() {
        int updatedCount = notificationService.markAllAsRead();
        return ResponseEntity.ok(ApiResponse.success(
                "All unread notifications marked as read",
                Map.of("updatedCount", updatedCount)
        ));
    }
}
