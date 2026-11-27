package com.skillbridge.service;

import com.skillbridge.dto.ExchangeRequestDTO;
import com.skillbridge.dto.ExchangeRequestResponse;
import com.skillbridge.entity.ExchangeRequest;
import com.skillbridge.entity.Student;
import com.skillbridge.enums.NotificationType;
import com.skillbridge.enums.RequestStatus;
import com.skillbridge.enums.UserRole;
import com.skillbridge.exception.BadRequestException;
import com.skillbridge.exception.DuplicateResourceException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.ConnectionRepository;
import com.skillbridge.repository.ExchangeRequestRepository;
import com.skillbridge.repository.StudentRepository;
import com.skillbridge.util.DtoMapper;
import com.skillbridge.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExchangeRequestService {

    private final ExchangeRequestRepository exchangeRequestRepository;
    private final StudentRepository studentRepository;
    private final ConnectionRepository connectionRepository;
    private final MatchService matchService;
    private final ConnectionService connectionService;
    private final NotificationService notificationService;

    public ExchangeRequestService(
            ExchangeRequestRepository exchangeRequestRepository,
            StudentRepository studentRepository,
            ConnectionRepository connectionRepository,
            MatchService matchService,
            ConnectionService connectionService,
            NotificationService notificationService
    ) {
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.studentRepository = studentRepository;
        this.connectionRepository = connectionRepository;
        this.matchService = matchService;
        this.connectionService = connectionService;
        this.notificationService = notificationService;
    }

    @Transactional
    public ExchangeRequestResponse sendRequest(ExchangeRequestDTO dto) {
        Long senderId = SecurityUtils.getCurrentStudentId();
        Long receiverId = dto.getReceiverId();

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("You cannot send an exchange request to yourself");
        }

        Student sender = studentRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender student not found"));

        Student receiver = studentRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver student not found with ID: " + receiverId));

        if (!receiver.isEnabled() || receiver.getRole() != UserRole.STUDENT) {
            throw new BadRequestException("Cannot send exchange request to inactive or non-student account");
        }

        if (connectionRepository.existsBetweenStudents(senderId, receiverId)) {
            throw new DuplicateResourceException("You are already connected with this student");
        }

        if (exchangeRequestRepository.existsBySenderIdAndReceiverIdAndStatus(senderId, receiverId, RequestStatus.PENDING)) {
            throw new DuplicateResourceException("A pending exchange request to this student already exists");
        }

        if (!matchService.isMutualMatch(senderId, receiverId)) {
            throw new BadRequestException("Exchange requests require a mutual skill match between sender and receiver");
        }

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setSender(sender);
        exchangeRequest.setReceiver(receiver);
        exchangeRequest.setMessage(dto.getMessage() != null ? dto.getMessage().trim() : null);
        exchangeRequest.setStatus(RequestStatus.PENDING);

        ExchangeRequest saved = exchangeRequestRepository.save(exchangeRequest);

        notificationService.createNotification(
                receiver,
                sender.getFullName() + " sent you a skill exchange request.",
                NotificationType.REQUEST_RECEIVED
        );

        return DtoMapper.toExchangeRequestResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ExchangeRequestResponse> getSentRequests(Pageable pageable) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return exchangeRequestRepository.findBySenderId(currentStudentId, pageable)
                .map(DtoMapper::toExchangeRequestResponse);
    }

    @Transactional(readOnly = true)
    public Page<ExchangeRequestResponse> getReceivedRequests(Pageable pageable) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        return exchangeRequestRepository.findByReceiverId(currentStudentId, pageable)
                .map(DtoMapper::toExchangeRequestResponse);
    }

    @Transactional(readOnly = true)
    public ExchangeRequestResponse getRequestById(Long requestId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        ExchangeRequest request = findRequestOrThrow(requestId);

        boolean isParticipant = request.getSender().getId().equals(currentStudentId)
                || request.getReceiver().getId().equals(currentStudentId);
        if (!isParticipant) {
            throw new AccessDeniedException("You do not have permission to view this exchange request");
        }

        return DtoMapper.toExchangeRequestResponse(request);
    }

    @Transactional
    public ExchangeRequestResponse acceptRequest(Long requestId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        ExchangeRequest request = findRequestOrThrow(requestId);

        if (!request.getReceiver().getId().equals(currentStudentId)) {
            throw new AccessDeniedException("Only the receiver can accept this exchange request");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BadRequestException("Only PENDING requests can be accepted. Current status: " + request.getStatus());
        }

        if (!matchService.isMutualMatch(request.getSender().getId(), request.getReceiver().getId())) {
            throw new BadRequestException("Mutual skill match is no longer valid between sender and receiver");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        ExchangeRequest saved = exchangeRequestRepository.save(request);

        notificationService.createNotification(
                request.getSender(),
                request.getReceiver().getFullName() + " accepted your skill exchange request!",
                NotificationType.REQUEST_ACCEPTED
        );

        connectionService.createConnectionFromAcceptedRequest(request.getSender(), request.getReceiver());

        return DtoMapper.toExchangeRequestResponse(saved);
    }

    @Transactional
    public ExchangeRequestResponse rejectRequest(Long requestId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        ExchangeRequest request = findRequestOrThrow(requestId);

        if (!request.getReceiver().getId().equals(currentStudentId)) {
            throw new AccessDeniedException("Only the receiver can reject this exchange request");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BadRequestException("Only PENDING requests can be rejected. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.REJECTED);
        ExchangeRequest saved = exchangeRequestRepository.save(request);

        notificationService.createNotification(
                request.getSender(),
                request.getReceiver().getFullName() + " rejected your skill exchange request.",
                NotificationType.REQUEST_REJECTED
        );

        return DtoMapper.toExchangeRequestResponse(saved);
    }

    @Transactional
    public ExchangeRequestResponse cancelRequest(Long requestId) {
        Long currentStudentId = SecurityUtils.getCurrentStudentId();
        ExchangeRequest request = findRequestOrThrow(requestId);

        if (!request.getSender().getId().equals(currentStudentId)) {
            throw new AccessDeniedException("Only the sender can cancel this exchange request");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BadRequestException("Only PENDING requests can be cancelled. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.CANCELLED);
        ExchangeRequest saved = exchangeRequestRepository.save(request);

        return DtoMapper.toExchangeRequestResponse(saved);
    }

    private ExchangeRequest findRequestOrThrow(Long requestId) {
        return exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request not found with ID: " + requestId));
    }
}
