package com.timur.taskmanagement.repositories;

import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.models.Role;
import com.timur.taskmanagement.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles"}) // Указываем, что нужно загружать roles
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String username);
    boolean  existsByRoles_Name(RoleUser roleUser);
}
