package com.skillbridge.controller;

import com.skillbridge.dto.ApiResponse;
import com.skillbridge.dto.ExchangeRequestDTO;
import com.skillbridge.dto.ExchangeRequestResponse;
import com.skillbridge.service.ExchangeRequestService;
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

@RestController
@RequestMapping("/api/requests")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Exchange Requests", description = "Send, accept, reject, and cancel skill exchange requests")
public class ExchangeRequestController {

    private final ExchangeRequestService exchangeRequestService;

    public ExchangeRequestController(ExchangeRequestService exchangeRequestService) {
        this.exchangeRequestService = exchangeRequestService;
    }

    @Operation(summary = "Send a skill exchange request to a mutually matched student")
    @PostMapping
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> sendRequest(
            @Valid @RequestBody ExchangeRequestDTO requestDTO
    ) {
        ExchangeRequestResponse created = exchangeRequestService.sendRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Exchange request sent successfully", created));
    }

    @Operation(summary = "Get paginated exchange requests sent by the logged-in student")
    @GetMapping("/sent")
    public ResponseEntity<ApiResponse<Page<ExchangeRequestResponse>>> getSentRequests(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Sent exchange requests fetched",
                exchangeRequestService.getSentRequests(pageable)
        ));
    }

    @Operation(summary = "Get paginated exchange requests received by the logged-in student")
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<Page<ExchangeRequestResponse>>> getReceivedRequests(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Received exchange requests fetched",
                exchangeRequestService.getReceivedRequests(pageable)
        ));
    }

    @Operation(summary = "Get details of an exchange request by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Exchange request fetched",
                exchangeRequestService.getRequestById(id)
        ));
    }

    @Operation(summary = "Accept a pending exchange request (Receiver only)")
    @PatchMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> acceptRequest(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Exchange request accepted and connection established",
                exchangeRequestService.acceptRequest(id)
        ));
    }

    @Operation(summary = "Reject a pending exchange request (Receiver only)")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Exchange request rejected",
                exchangeRequestService.rejectRequest(id)
        ));
    }

    @Operation(summary = "Cancel a pending exchange request (Sender only)")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> cancelRequest(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Exchange request cancelled",
                exchangeRequestService.cancelRequest(id)
        ));
    }
}
