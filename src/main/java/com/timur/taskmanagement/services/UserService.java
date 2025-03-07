package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.RegisterRequestDTO;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public void save(User user){
        userRepository.save(user);
    }
    public User findUserByEmail(String email){
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.orElseThrow(()-> new RuntimeException("User with this email was not found"));
    }

    public User findUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(()-> new RuntimeException("User with this id was not found"));
    }

    public void delete(User user){
        userRepository.delete(user);
    }
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }
    public boolean existsUserByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
