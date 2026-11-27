package com.skillbridge.repository;

import com.skillbridge.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByStudentIdOrderByCreatedAtDesc(Long studentId, Pageable pageable);

    List<Notification> findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(Long studentId);

    Optional<Notification> findByIdAndStudentId(Long id, Long studentId);

    long countByStudentIdAndIsReadFalse(Long studentId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.student.id = :studentId AND n.isRead = false")
    int markAllAsReadByStudentId(@Param("studentId") Long studentId);
}
