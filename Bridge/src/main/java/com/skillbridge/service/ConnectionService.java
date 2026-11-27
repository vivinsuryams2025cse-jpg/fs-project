package com.skillbridge.service;

import com.skillbridge.dto.ConnectionResponse;
import com.skillbridge.entity.Connection;
import com.skillbridge.entity.Student;
import com.skillbridge.enums.NotificationType;
import com.skillbridge.exception.BadRequestException;
import com.skillbridge.exception.DuplicateResourceException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.ConnectionRepository;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final NotificationService notificationService;

    public ConnectionService(ConnectionRepository connectionRepository, NotificationService notificationService) {
        this.connectionRepository = connectionRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Connection createConnectionFromAcceptedRequest(Student studentA, Student studentB) {
        if (studentA.getId().equals(studentB.getId())) {
            throw new BadRequestException("Cannot create connection with oneself");
        }

        if (connectionRepository.existsBetweenStudents(studentA.getId(), studentB.getId())) {
            throw new DuplicateResourceException("Students are already connected");
        }

        Student first = studentA.getId() < studentB.getId() ? studentA : studentB;
        Student second = studentA.getId() < studentB.getId() ? studentB : studentA;

        Connection connection = new Connection(first, second);
        Connection saved = connectionRepository.save(connection);

        notificationService.createNotification(
                studentA,
                "You are now connected with " + studentB.getFullName() + " for skill exchange!",
                NotificationType.CONNECTION_CREATED
        );
        notificationService.createNotification(
                studentB,
                "You are now connected with " + studentA.getFullName() + " for skill exchange!",
                NotificationType.CONNECTION_CREATED
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public Page<ConnectionResponse> getMyConnections(Pageable pageable) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return connectionRepository.findAllByStudentId(currentStudentId, pageable)
                .map(c -> DtoMapper.toConnectionResponse(c, currentStudentId));
    }

    @Transactional(readOnly = true)
    public ConnectionResponse getConnectionById(Long connectionId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with ID: " + connectionId));

        validateParticipant(connection, currentStudentId);
        return DtoMapper.toConnectionResponse(connection, currentStudentId);
    }

    @Transactional
    public void deleteConnection(Long connectionId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with ID: " + connectionId));

        validateParticipant(connection, currentStudentId);
        connectionRepository.delete(connection);
    }

    private void validateParticipant(Connection connection, Long studentId) {
        boolean isParticipant = connection.getStudentOne().getId().equals(studentId)
                || connection.getStudentTwo().getId().equals(studentId);
        if (!isParticipant) {
            throw new AccessDeniedException("You are not a participant in this connection");
        }
    }
}
