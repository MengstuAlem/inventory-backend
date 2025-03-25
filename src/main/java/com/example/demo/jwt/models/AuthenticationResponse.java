package com.example.demo.jwt.models;

import lombok.AllArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {
    private String token;
}
