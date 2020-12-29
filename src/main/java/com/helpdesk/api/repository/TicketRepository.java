package com.helpdesk.api.repository;

import com.helpdesk.api.domain.Ticket;
import com.helpdesk.api.domain.enums.PriorityEnum;
import com.helpdesk.api.domain.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    Page<Ticket> findByUserIdOrderByDateDesc(String userId, Pageable pages);

    Page<Ticket> findByNumber(Integer number, Pageable pages);

    Page<Ticket> findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(
            String title, StatusEnum status, PriorityEnum priority, Pageable pageable);

    Page<Ticket> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndUserIdOrderByDateDesc(
            String title, StatusEnum status, PriorityEnum priority, String user_id, Pageable pageable);

    Page<Ticket> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndAssignedUserIdOrderByDateDesc(
            String title, StatusEnum status, PriorityEnum priority, String assignedUser_id, Pageable pageable);
}
