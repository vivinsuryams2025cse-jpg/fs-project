package com.skillbridge.dto;

public class AdminDashboardResponse {

    private long totalStudents;
    private long activeStudents;
    private long totalUniqueSkills;
    private long totalStudentSkillMappings;
    private long totalExchangeRequests;
    private long pendingExchangeRequests;
    private long acceptedExchangeRequests;
    private long rejectedExchangeRequests;
    private long totalConnections;

    public AdminDashboardResponse() {
    }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getActiveStudents() { return activeStudents; }
    public void setActiveStudents(long activeStudents) { this.activeStudents = activeStudents; }

    public long getTotalUniqueSkills() { return totalUniqueSkills; }
    public void setTotalUniqueSkills(long totalUniqueSkills) { this.totalUniqueSkills = totalUniqueSkills; }

    public long getTotalStudentSkillMappings() { return totalStudentSkillMappings; }
    public void setTotalStudentSkillMappings(long totalStudentSkillMappings) { this.totalStudentSkillMappings = totalStudentSkillMappings; }

    public long getTotalExchangeRequests() { return totalExchangeRequests; }
    public void setTotalExchangeRequests(long totalExchangeRequests) { this.totalExchangeRequests = totalExchangeRequests; }

    public long getPendingExchangeRequests() { return pendingExchangeRequests; }
    public void setPendingExchangeRequests(long pendingExchangeRequests) { this.pendingExchangeRequests = pendingExchangeRequests; }

    public long getAcceptedExchangeRequests() { return acceptedExchangeRequests; }
    public void setAcceptedExchangeRequests(long acceptedExchangeRequests) { this.acceptedExchangeRequests = acceptedExchangeRequests; }

    public long getRejectedExchangeRequests() { return rejectedExchangeRequests; }
    public void setRejectedExchangeRequests(long rejectedExchangeRequests) { this.rejectedExchangeRequests = rejectedExchangeRequests; }

    public long getTotalConnections() { return totalConnections; }
    public void setTotalConnections(long totalConnections) { this.totalConnections = totalConnections; }
}
