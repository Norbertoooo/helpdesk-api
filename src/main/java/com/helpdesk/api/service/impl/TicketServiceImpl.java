package com.helpdesk.api.service.impl;

import com.helpdesk.api.domain.ChangeStatus;
import com.helpdesk.api.domain.Ticket;
import com.helpdesk.api.domain.enums.PriorityEnum;
import com.helpdesk.api.domain.enums.StatusEnum;
import com.helpdesk.api.repository.ChangeStatusRepository;
import com.helpdesk.api.repository.TicketRepository;
import com.helpdesk.api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ChangeStatusRepository changeStatusRepository;

    public Ticket createOrUpdate(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket findById(String id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    public void delete(String id) {
        this.ticketRepository.deleteById(id);
    }

    public Page<Ticket> listTicket(int page, int count) {
        Pageable pages = PageRequest.of(page, count);
        return this.ticketRepository.findAll(pages);
    }

    public Iterable<Ticket> findAll() {
        return this.ticketRepository.findAll();
    }

    public Page<Ticket> findByCurrentUser(int page, int count, String userId) {
        Pageable pages = PageRequest.of(page, count);
        return this.ticketRepository.findByUserIdOrderByDateDesc(userId, pages);
    }

    public ChangeStatus createChangeStatus(ChangeStatus changeStatus) {
        return this.changeStatusRepository.save(changeStatus);
    }

    public Iterable<ChangeStatus> listChangeStatus(String ticketId) {
        return this.changeStatusRepository.findByTicketIdOrderByDateChangeStatusDesc(ticketId);
    }

    public Page<Ticket> findByParameters(int page, int count, String title, StatusEnum status, PriorityEnum priority) {
        Pageable pages = PageRequest.of(page, count);
        return ticketRepository
                .findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByDateDesc(title, status, priority, pages);
    }

    public Page<Ticket> findByParametersAndCurrentUser(int page, int count,String title,StatusEnum status,
                                                       PriorityEnum priority,String userId) {
        Pageable pages = PageRequest.of(page, count);
        return this.ticketRepository.
                findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndUserIdOrderByDateDesc(
                        title,status,priority,userId,pages);
    }

    public Page<Ticket> findByNumber(int page, int count,Integer number){
        Pageable pages = PageRequest.of(page, count);
        return this.ticketRepository.findByNumber(number, pages);
    }

    public Page<Ticket> findByParametersAndAssignedUser(int page, int count,String title,StatusEnum status,
                                                        PriorityEnum priority,String assignedUserId) {
        Pageable pages = PageRequest.of(page, count);
        return this.ticketRepository.
                findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndAssignedUserIdOrderByDateDesc(
                        title,status,priority,assignedUserId,pages);
    }
}
