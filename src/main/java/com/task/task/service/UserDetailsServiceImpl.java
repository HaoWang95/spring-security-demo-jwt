package com.task.task.service;

import com.task.task.models.User;
import com.task.task.models.UserDetailsImpl;
import com.task.task.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                // In UserRepository, we used Optional<User>, needs to check if it is null
                .orElseThrow(() -> new UsernameNotFoundException("User data not found"));
        // By using the static UserDetailsImpl.build(userInstance), build a UserDetails instance
        return UserDetailsImpl.build(user);
    }
}
