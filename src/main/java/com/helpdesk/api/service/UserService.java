package com.helpdesk.api.service;

import com.helpdesk.api.domain.User;
import org.springframework.data.domain.Page;

public interface UserService {

    User findByEmail(String email);

    User createOrUpdate(User user);

    User findById(String id);

    void deleteById(String id);

    Page<User> findAll(int page, int count);
}
