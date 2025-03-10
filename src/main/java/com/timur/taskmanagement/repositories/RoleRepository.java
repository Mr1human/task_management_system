package com.timur.taskmanagement.repositories;

import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleUser name);
    boolean existsByName(RoleUser roleUser);
}
