package com.helpdesk.api.repository;

import com.helpdesk.api.domain.ChangeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String> {
    Iterable<ChangeStatus> findByTicketIdOrderByDateChangeStatusDesc(String ticketId);
}
