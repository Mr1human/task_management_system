package com.timur.taskmanagement.services.impl;

import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email).orElseThrow(()-> new RuntimeException("User with this email was not found"));
        return UserDetailsImpl.build(user);
    }
}
