package com.helpdesk.api.service;

import com.helpdesk.api.domain.ChangeStatus;
import com.helpdesk.api.domain.Ticket;
import com.helpdesk.api.domain.enums.PriorityEnum;
import com.helpdesk.api.domain.enums.StatusEnum;
import org.springframework.data.domain.Page;

public interface TicketService {

    Ticket createOrUpdate(Ticket ticket);

    Ticket findById(String id);

    void delete(String id);

    Page<Ticket> listTicket(int page, int count);

    ChangeStatus createChangeStatus(ChangeStatus changeStatus);

    Iterable<ChangeStatus> listChangeStatus(String ticketId);

    Page<Ticket> findByCurrentUser(int page, int count, String userId);

    Page<Ticket> findByParameters(int page, int count, String title, StatusEnum status, PriorityEnum priority);

    Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, StatusEnum status, PriorityEnum priority, String userId);

    Page<Ticket> findByNumber(int page, int count, Integer number);

    Iterable<Ticket> findAll();

    Page<Ticket> findByParametersAndAssignedUser(int page, int count, String title, StatusEnum status, PriorityEnum priority, String assignedUserId);
}