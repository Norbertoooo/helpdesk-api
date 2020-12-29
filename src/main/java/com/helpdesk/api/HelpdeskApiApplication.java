package com.helpdesk.api;

import com.helpdesk.api.domain.User;
import com.helpdesk.api.domain.enums.ProfileEnum;
import com.helpdesk.api.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Log4j2
public class HelpdeskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskApiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
          initUsers(userRepository, passwordEncoder);
        };
    }

    private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        log.info("inserindo usuario administrador");
        User admin = new User("","admin@gmail.com", passwordEncoder.encode("123456"), ProfileEnum.ROLE_ADMIN);
        userRepository.save(admin);
    }

}
