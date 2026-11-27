package com.skillbridge.dto;

import com.skillbridge.enums.RequestStatus;

import java.time.LocalDateTime;

public class ExchangeRequestResponse {

    private Long id;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    private String senderCollege;
    private Long receiverId;
    private String receiverName;
    private String receiverEmail;
    private String receiverCollege;
    private String message;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExchangeRequestResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getSenderCollege() { return senderCollege; }
    public void setSenderCollege(String senderCollege) { this.senderCollege = senderCollege; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }

    public String getReceiverCollege() { return receiverCollege; }
    public void setReceiverCollege(String receiverCollege) { this.receiverCollege = receiverCollege; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
