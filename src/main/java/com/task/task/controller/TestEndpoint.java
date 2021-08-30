package com.task.task.controller;

import com.task.task.models.Role;
import com.task.task.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/test")
public class TestEndpoint {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String testIndex(){
        return "Test endpoint";
    }

    @GetMapping("/testRoles")
    public ResponseEntity<?> findRolesTest(){
        return ResponseEntity.ok(List.of(roleRepository.findAll()));
    }

    @GetMapping("/greeting")
    public String greeting(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return "Hello, "+ authentication.getName();
    }

    @GetMapping("/roles")
    public ResponseEntity<?> findRoles(){
        return ResponseEntity.ok(List.of(roleRepository.findAll()));
    }
}
