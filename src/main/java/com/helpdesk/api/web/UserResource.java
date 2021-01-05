package com.helpdesk.api.web;

import com.helpdesk.api.domain.User;
import com.helpdesk.api.service.UserService;
import com.helpdesk.api.web.dto.ResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@Log4j2
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO<User>> create(HttpServletRequest request, @RequestBody User user, BindingResult result) {
        ResponseDTO<User> response = new ResponseDTO<>();
        // que bagulho feio, tratamento feito com try catch no controller
        try {
            validateCreatedUser(user,result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach( objectError -> response.getErros().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            response.setData(userService.createOrUpdate(user));
        }
        catch (DuplicateKeyException duplicateKeyException) {
            response.getErros().add("Email ja cadastrado");
            return ResponseEntity.badRequest().body(response);
        }
        catch (Exception exception) {
            response.getErros().add(exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO<User>> update(HttpServletRequest request, @RequestBody User user, BindingResult result) {
        ResponseDTO<User> response = new ResponseDTO<>();
        // que bagulho feio, tratamento feito com try catch no controller
        try {
            validateUpdatedUser(user,result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach( objectError -> response.getErros().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            response.setData(userService.createOrUpdate(user));
        }
        catch (Exception exception) {
            response.getErros().add(exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO<User>> findById(@PathVariable String id) {
        ResponseDTO<User> response = new ResponseDTO<>();
        User user = userService.findById(id);
        if (user == null) {
            response.getErros().add("Usuario nao encontrado para o id informado");
            return ResponseEntity.badRequest().body(response);
        }
        response.setData(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{page}/{count}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO<Page<User>>> findAll(@PathVariable int page, @PathVariable int count) {
        ResponseDTO<Page<User>> response = new ResponseDTO<>();
        Page<User> users = userService.findAll(page,count);
        response.setData(users);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDTO<User>> deleteById(@PathVariable String id) {
        ResponseDTO<User> response = new ResponseDTO<>();
        User user = userService.findById(id);
        if (user == null) {
            response.getErros().add("Usuario nao encontrado para o id informado");
            return ResponseEntity.badRequest().body(response);
        }
        response.setData(user);
        return ResponseEntity.ok(response);
    }

    private void validateCreatedUser(User user, BindingResult result) {
        if(user.getEmail() == null) {
            result.addError(new ObjectError("User","Email nao informado"));
        }
    }

    private void validateUpdatedUser(User user, BindingResult result) {
        if(user.getId() == null) {
            result.addError(new ObjectError("User","id nao informado"));
        }
        if(user.getEmail() == null) {
            result.addError(new ObjectError("User","Email nao informado"));
        }
    }
}
