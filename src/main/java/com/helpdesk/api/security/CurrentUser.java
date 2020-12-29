package com.helpdesk.api.security;

import com.helpdesk.api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser {

    private String token;
    private User user;
}
