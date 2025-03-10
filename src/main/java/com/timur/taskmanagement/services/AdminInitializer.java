package com.timur.taskmanagement.services;

import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.models.Role;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AdminInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public AdminInitializer(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public void initializeAdmin(){

        Set<RoleUser> defaultRoles = Set.of(RoleUser.ROLE_USER, RoleUser.ROLE_ADMIN); //вынести в миграцию

        for (RoleUser roleName : defaultRoles) {
            if (!roleService.existsRoleByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleService.save(role);
            }
        }

        if (!userService.isAdminExists()){
            User admin = new User();
            admin.setEmail("admin@mail.ru");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.getRoles().add(roleService.findByName(RoleUser.ROLE_ADMIN));
            userService.save(admin);
        }
    }
}
