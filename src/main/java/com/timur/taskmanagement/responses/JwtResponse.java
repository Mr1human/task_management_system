package com.timur.taskmanagement.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {

    private String jwtAccessToken;

    private final String type = "JWT";
    private String email;
    private Long userId;

    public JwtResponse(String jwtAccesstoken, String email, Long userId) {
        this.jwtAccessToken = jwtAccesstoken;
        this.email = email;
        this.userId = userId;
    }
}
