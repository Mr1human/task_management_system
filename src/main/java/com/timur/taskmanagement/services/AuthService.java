package com.timur.taskmanagement.services;

import com.timur.taskmanagement.dto.LoginRequestDTO;
import com.timur.taskmanagement.dto.RegisterRequestDTO;
import com.timur.taskmanagement.enums.RoleUser;
import com.timur.taskmanagement.exceptions.EmailAlreadyTakenException;
import com.timur.taskmanagement.exceptions.InvalidCredentialsException;
import com.timur.taskmanagement.jwt.JwtUtils;
import com.timur.taskmanagement.models.Role;
import com.timur.taskmanagement.models.User;
import com.timur.taskmanagement.responses.JwtResponse;
import com.timur.taskmanagement.responses.UserRegisterResponse;
import com.timur.taskmanagement.services.impl.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {
    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final RoleService roleService;

    public AuthService(JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    public UserRegisterResponse register(RegisterRequestDTO registerRequest) {

        if (userService.existsUserByEmail(registerRequest.getEmail()))
            throw new EmailAlreadyTakenException("Email is already taken: " + registerRequest.getEmail());

        Role role = roleService.findByName(RoleUser.ROLE_USER);
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.getRoles().add(role);

        userService.save(user);

        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();

        userRegisterResponse.setEmail(user.getEmail());
        userRegisterResponse.setId(user.getId().toString());
        userRegisterResponse.setRoles(user.getRoles()
                .stream()
                .map(roleUser-> roleUser.getName().name()).collect(Collectors.toList()));
        userRegisterResponse.setMessage("User is registered!");

        return userRegisterResponse;
    }

    public JwtResponse login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userService.findUserByEmail(userDetails.getUsername());

            String jwtAccessToken = jwtUtils.generateAccessToken(user);

            return new JwtResponse(jwtAccessToken, user.getEmail());
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetailsImpl)authentication.getPrincipal();
        User currentUser = userService.findUserByEmail(userDetails.getUsername());
        return currentUser;
    }
}
