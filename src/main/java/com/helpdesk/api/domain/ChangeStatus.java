package com.helpdesk.api.domain;

import com.helpdesk.api.domain.enums.StatusEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class ChangeStatus {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Ticket ticket;

    @DBRef(lazy = true)
    private User userChange;

    private Date dateChangeStatus;

    private StatusEnum status;
}
