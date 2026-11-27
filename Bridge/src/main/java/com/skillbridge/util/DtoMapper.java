package com.skillbridge.util;

import com.skillbridge.dto.*;
import com.skillbridge.entity.*;
import com.skillbridge.enums.SkillType;

import java.util.List;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static SkillResponse toSkillResponse(StudentSkill studentSkill) {
        return new SkillResponse(
                studentSkill.getId(),
                studentSkill.getSkill().getId(),
                studentSkill.getSkill().getSkillName(),
                studentSkill.getSkillType(),
                studentSkill.getStudent().getId(),
                studentSkill.getStudent().getFullName()
        );
    }

    public static SkillResponse toCatalogSkillResponse(Skill skill) {
        SkillResponse response = new SkillResponse();
        response.setSkillId(skill.getId());
        response.setSkillName(skill.getSkillName());
        return response;
    }

    public static StudentResponse toStudentResponse(Student student, List<StudentSkill> skills) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFullName(student.getFullName());
        response.setEmail(student.getEmail());
        response.setCollegeName(student.getCollegeName());
        response.setDepartment(student.getDepartment());
        response.setYearOfStudy(student.getYearOfStudy());
        response.setBio(student.getBio());
        response.setRole(student.getRole());
        response.setEnabled(student.isEnabled());
        response.setCreatedAt(student.getCreatedAt());

        if (skills != null) {
            response.setTeachSkills(
                    skills.stream()
                            .filter(s -> s.getSkillType() == SkillType.TEACH)
                            .map(DtoMapper::toSkillResponse)
                            .toList()
            );
            response.setLearnSkills(
                    skills.stream()
                            .filter(s -> s.getSkillType() == SkillType.LEARN)
                            .map(DtoMapper::toSkillResponse)
                            .toList()
            );
        }
        return response;
    }

    public static ExchangeRequestResponse toExchangeRequestResponse(ExchangeRequest request) {
        ExchangeRequestResponse response = new ExchangeRequestResponse();
        response.setId(request.getId());
        response.setSenderId(request.getSender().getId());
        response.setSenderName(request.getSender().getFullName());
        response.setSenderEmail(request.getSender().getEmail());
        response.setSenderCollege(request.getSender().getCollegeName());
        response.setReceiverId(request.getReceiver().getId());
        response.setReceiverName(request.getReceiver().getFullName());
        response.setReceiverEmail(request.getReceiver().getEmail());
        response.setReceiverCollege(request.getReceiver().getCollegeName());
        response.setMessage(request.getMessage());
        response.setStatus(request.getStatus());
        response.setCreatedAt(request.getCreatedAt());
        response.setUpdatedAt(request.getUpdatedAt());
        return response;
    }

    public static ConnectionResponse toConnectionResponse(Connection connection, Long viewerStudentId) {
        ConnectionResponse response = new ConnectionResponse();
        response.setId(connection.getId());
        response.setStudentOneId(connection.getStudentOne().getId());
        response.setStudentOneName(connection.getStudentOne().getFullName());
        response.setStudentOneEmail(connection.getStudentOne().getEmail());
        response.setStudentTwoId(connection.getStudentTwo().getId());
        response.setStudentTwoName(connection.getStudentTwo().getFullName());
        response.setStudentTwoEmail(connection.getStudentTwo().getEmail());
        response.setConnectedAt(connection.getConnectedAt());

        if (viewerStudentId != null) {
            Student partner = connection.getStudentOne().getId().equals(viewerStudentId)
                    ? connection.getStudentTwo()
                    : connection.getStudentOne();
            response.setPartnerId(partner.getId());
            response.setPartnerName(partner.getFullName());
            response.setPartnerEmail(partner.getEmail());
            response.setPartnerCollege(partner.getCollegeName());
            response.setPartnerDepartment(partner.getDepartment());
        }
        return response;
    }

    public static NotificationResponse toNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setStudentId(notification.getStudent().getId());
        response.setMessage(notification.getMessage());
        response.setNotificationType(notification.getNotificationType());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
