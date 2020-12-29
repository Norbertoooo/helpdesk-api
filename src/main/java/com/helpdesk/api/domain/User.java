package com.helpdesk.api.domain;

import com.helpdesk.api.domain.enums.ProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Document
@Data
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private ProfileEnum profile;
}
