package com.timur.taskmanagement.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {

    private String jwtAccessToken;

    private final String type = "JWT";
    private String email;

    public JwtResponse(String jwtAccesstoken, String email) {
        this.jwtAccessToken = jwtAccesstoken;
        this.email = email;
    }
}
