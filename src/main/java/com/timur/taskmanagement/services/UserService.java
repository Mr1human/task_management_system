package com.timur.taskmanagement.services;

import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(()-> new EntityNotFoundException("User with this email was not found"));
    }

    public User findUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(()-> new EntityNotFoundException("User with this id was not found"));
    }

    public boolean isAdmin(User user){
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleUser.ROLE_ADMIN));
    }

    public boolean isAdminExists(){
            return userRepository.existsByRoles_Name(RoleUser.ROLE_ADMIN);
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
