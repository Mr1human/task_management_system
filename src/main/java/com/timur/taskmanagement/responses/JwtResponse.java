package com.timur.taskmanagement.responses;

public class JwtResponse {

    private String jwtAccessToken;

    private final String type = "JWT";
    private String email;

    public JwtResponse(String jwtAccesstoken, String email) {
        this.jwtAccessToken = jwtAccesstoken;
        this.email = email;
    }
}
