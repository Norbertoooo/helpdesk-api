package com.helpdesk.api.domain;

import com.helpdesk.api.domain.enums.PriorityEnum;
import com.helpdesk.api.domain.enums.StatusEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Data
public class Ticket {

    @Id
    private String id;

    @DBRef(lazy = true)
    private User user;

    private Date date = new Date();

    private String title;

    private Integer number;

    @DBRef(lazy = true)
    private User assignedUser;

    private String image;

    private String description;

    @DBRef(lazy = true)
    private StatusEnum status;

    @DBRef(lazy = true)
    private PriorityEnum priority;

    @Transient
    private List<ChangeStatus> changes;

}
