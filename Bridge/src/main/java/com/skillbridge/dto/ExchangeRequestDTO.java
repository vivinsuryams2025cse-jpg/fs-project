package com.skillbridge.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExchangeRequestDTO {

    @NotNull(message = "Receiver student ID is required")
    private Long receiverId;

    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    public ExchangeRequestDTO() {
    }

    public ExchangeRequestDTO(Long receiverId, String message) {
        this.receiverId = receiverId;
        this.message = message;
    }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
