package com.skillbridge.repository;

import com.skillbridge.entity.ExchangeRequest;
import com.skillbridge.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {

    Page<ExchangeRequest> findBySenderId(Long senderId, Pageable pageable);

    Page<ExchangeRequest> findByReceiverId(Long receiverId, Pageable pageable);

    List<ExchangeRequest> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    List<ExchangeRequest> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, RequestStatus status);

    long countByStatus(RequestStatus status);
}
