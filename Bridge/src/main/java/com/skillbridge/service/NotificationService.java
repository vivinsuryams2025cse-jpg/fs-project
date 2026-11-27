package com.skillbridge.service;

import com.skillbridge.dto.NotificationResponse;
import com.skillbridge.entity.Notification;
import com.skillbridge.entity.Student;
import com.skillbridge.enums.NotificationType;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.NotificationRepository;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void createNotification(Student recipient, String message, NotificationType type) {
        Notification notification = new Notification(recipient, message, type);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(Pageable pageable) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return notificationRepository.findByStudentIdOrderByCreatedAtDesc(currentStudentId, pageable)
                .map(DtoMapper::toNotificationResponse);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyUnreadNotifications() {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return notificationRepository.findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(currentStudentId)
                .stream()
                .map(DtoMapper::toNotificationResponse)
                .toList();
    }

    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        Notification notification = notificationRepository.findByIdAndStudentId(notificationId, currentStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        notification.setRead(true);
        Notification saved = notificationRepository.save(notification);
        return DtoMapper.toNotificationResponse(saved);
    }

    @Transactional
    public int markAllAsRead() {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return notificationRepository.markAllAsReadByStudentId(currentStudentId);
    }
}
