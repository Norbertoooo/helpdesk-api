package com.helpdesk.api.web;

import com.helpdesk.api.domain.ChangeStatus;
import com.helpdesk.api.domain.Ticket;
import com.helpdesk.api.domain.User;
import com.helpdesk.api.security.JwtTokenUtil;
import com.helpdesk.api.service.TicketService;
import com.helpdesk.api.service.UserService;
import com.helpdesk.api.web.dto.ResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.helpdesk.api.domain.enums.ProfileEnum.ROLE_CUSTOMER;
import static com.helpdesk.api.domain.enums.ProfileEnum.ROLE_TECHNICIAN;
import static com.helpdesk.api.domain.enums.StatusEnum.NEW;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
@Log4j2
public class TicketResource {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result) {
        ResponseDTO<Ticket> response = new ResponseDTO<>();
        // que bagulho feio, tratamento feito com try catch no controller
        try {
            validateCreatedTicket(ticket, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError -> response.getErros().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            ticket.setStatus(NEW);
            ticket.setUser(userFromRequest(request));
            ticket.setNumber(generateNumber());
            response.setData(ticketService.createOrUpdate(ticket));
        } catch (Exception exception) {
            response.getErros().add(exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result) {
        ResponseDTO<Ticket> response = new ResponseDTO<>();
        // que bagulho feio, tratamento feito com try catch no controller
        try {
            validateUpdatedTicket(ticket, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError -> response.getErros().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            ticket.setStatus(NEW);
            ticket.setUser(userFromRequest(request));
            ticket.setNumber(generateNumber());
            response.setData(ticketService.createOrUpdate(ticket));
        } catch (Exception exception) {
            response.getErros().add(exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<ResponseDTO<Ticket>> findById(@PathVariable String id) {
        ResponseDTO<Ticket> response = new ResponseDTO<>();
        Ticket ticket = ticketService.findById(id);
        if (ticket == null) {
            response.getErros().add("Ticket nao encontrado para o id informado");
            return ResponseEntity.badRequest().body(response);
        }
        List<ChangeStatus> changes = new ArrayList<>();
        Iterable<ChangeStatus> iterable = ticketService.listChangeStatus(ticket.getId());
        iterable.forEach(changeStatus -> {
            changeStatus.setTicket(null);
            changes.add(changeStatus);
        });
        ticket.setChanges(changes);
        response.setData(ticket);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO<Ticket>> deleteById(@PathVariable String id) {
        ResponseDTO<Ticket> response = new ResponseDTO<>();
        Ticket ticket = ticketService.findById(id);
        if (ticket == null) {
            response.getErros().add("Ticket nao encontrado para o id informado");
            return ResponseEntity.badRequest().body(response);
        }
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{page}/{count}")
    @PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
    public ResponseEntity<ResponseDTO<Page<Ticket>>> findAll(HttpServletRequest request, @PathVariable Integer page, @PathVariable Integer count) {
        ResponseDTO<Page<Ticket>> response = new ResponseDTO<>();
        User userRequest = userFromRequest(request);
        Page<Ticket> ticketPage = null;
        if (userRequest.getProfile().equals(ROLE_TECHNICIAN)) {
            ticketPage = ticketService.listTicket(page, count);
        } else {
            if (userRequest.getProfile().equals(ROLE_CUSTOMER)) {
                ticketPage = ticketService.findByCurrentUser(page, count, userRequest.getId());
            }
        }
        response.setData(ticketPage);
        return ResponseEntity.ok(response);
    }

    private Integer generateNumber() {
        return new Random().nextInt(9999);
    }

    private User userFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return userService.findByEmail(email);
    }

    private void validateCreatedTicket(Ticket ticket, BindingResult result) {
        if (ticket.getTitle() == null) {
            result.addError(new ObjectError("Ticket", "Titulo nao informado"));
        }
    }

    private void validateUpdatedTicket(Ticket ticket, BindingResult result) {
        if (ticket.getId() == null) {
            result.addError(new ObjectError("Ticket", "Id nao informado"));
        }
        if (ticket.getTitle() == null) {
            result.addError(new ObjectError("Ticket", "Titulo nao informado"));
        }
    }
}
