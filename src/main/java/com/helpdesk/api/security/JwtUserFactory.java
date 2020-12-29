package com.helpdesk.api.security;

import com.helpdesk.api.domain.User;
import com.helpdesk.api.domain.enums.ProfileEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), mapGrantedAuthorities(user.getProfile()));
    }

    private static List<GrantedAuthority> mapGrantedAuthorities(ProfileEnum profile) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(profile.toString()));
        return authorities;
    }
}
