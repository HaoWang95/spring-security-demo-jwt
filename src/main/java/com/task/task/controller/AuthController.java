package com.task.task.controller;

import com.task.task.dto.JwtResponse;
import com.task.task.dto.LoginRequestDto;
import com.task.task.dto.SignUpRequestDto;
import com.task.task.models.Role;
import com.task.task.models.RoleType;
import com.task.task.models.User;
import com.task.task.models.UserDetailsImpl;
import com.task.task.repositories.RoleRepository;
import com.task.task.repositories.UserRepository;
import com.task.task.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto signUpBody){
        if(userRepository.existsByUsername(signUpBody.getUsername())){
            return ResponseEntity.badRequest().body("Username exists");
        }
        if(userRepository.existsByEmail(signUpBody.getEmail())){
            return ResponseEntity.badRequest().body("Email exists");
        }

        User user = new User(
                signUpBody.getUsername(),
                signUpBody.getEmail(),
                encoder.encode(signUpBody.getPassword())
                );
        Set<String> roles = signUpBody.getRoles();
        Set<Role> roleSet = new HashSet<Role>();
        if(roles == null){
            // By default, set it to user
            Role userRole = roleRepository.findByRoleType(RoleType.user).orElseThrow();
            roleSet.add(userRole);
        }else {
            roles.forEach(role -> {
                if (role.equals("admin")){
                    Role admin = roleRepository.findByRoleType(RoleType.admin).orElseThrow();
                    roleSet.add(admin);
                }
                if(role.equals("user")){
                    Role client = roleRepository.findByRoleType(RoleType.user).orElseThrow();
                    roleSet.add(client);
                }
            });
        }
        user.setRoles(roleSet);
        userRepository.save(user);
        return ResponseEntity.ok("200 Ok");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.getUsername(),
                                loginRequestDto.getPassword()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwt(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        JwtResponse response = new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
        return ResponseEntity.ok(response);
    }
}
