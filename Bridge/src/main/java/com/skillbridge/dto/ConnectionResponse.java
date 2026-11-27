package com.skillbridge.dto;

import java.time.LocalDateTime;

public class ConnectionResponse {

    private Long id;
    private Long studentOneId;
    private String studentOneName;
    private String studentOneEmail;
    private Long studentTwoId;
    private String studentTwoName;
    private String studentTwoEmail;
    private Long partnerId;
    private String partnerName;
    private String partnerEmail;
    private String partnerCollege;
    private String partnerDepartment;
    private LocalDateTime connectedAt;

    public ConnectionResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentOneId() { return studentOneId; }
    public void setStudentOneId(Long studentOneId) { this.studentOneId = studentOneId; }

    public String getStudentOneName() { return studentOneName; }
    public void setStudentOneName(String studentOneName) { this.studentOneName = studentOneName; }

    public String getStudentOneEmail() { return studentOneEmail; }
    public void setStudentOneEmail(String studentOneEmail) { this.studentOneEmail = studentOneEmail; }

    public Long getStudentTwoId() { return studentTwoId; }
    public void setStudentTwoId(Long studentTwoId) { this.studentTwoId = studentTwoId; }

    public String getStudentTwoName() { return studentTwoName; }
    public void setStudentTwoName(String studentTwoName) { this.studentTwoName = studentTwoName; }

    public String getStudentTwoEmail() { return studentTwoEmail; }
    public void setStudentTwoEmail(String studentTwoEmail) { this.studentTwoEmail = studentTwoEmail; }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }

    public String getPartnerEmail() { return partnerEmail; }
    public void setPartnerEmail(String partnerEmail) { this.partnerEmail = partnerEmail; }

    public String getPartnerCollege() { return partnerCollege; }
    public void setPartnerCollege(String partnerCollege) { this.partnerCollege = partnerCollege; }

    public String getPartnerDepartment() { return partnerDepartment; }
    public void setPartnerDepartment(String partnerDepartment) { this.partnerDepartment = partnerDepartment; }

    public LocalDateTime getConnectedAt() { return connectedAt; }
    public void setConnectedAt(LocalDateTime connectedAt) { this.connectedAt = connectedAt; }
}
