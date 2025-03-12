package com.timur.taskmanagement.services;

import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.models.Role;
import com.timur.taskmanagement.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(Role role){
        roleRepository.save(role);
    }

    public Role findByName(RoleUser role){
        return roleRepository
                .findByName(role).orElseThrow(() -> new EntityNotFoundException("role not found"));
    }

    public boolean existsRoleByName(RoleUser roleUser){
        return roleRepository.existsByName(roleUser);
    }
}
