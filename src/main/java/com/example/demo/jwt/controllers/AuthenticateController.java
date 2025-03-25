package com.example.demo.jwt.controllers;

import com.example.demo.User.ProfileOfUser;
import com.example.demo.jwt.models.AuthenticationRequest;
import com.example.demo.jwt.models.AuthenticationResponse;
import com.example.demo.jwt.services.ApplicationUserDetailsService;
import com.example.demo.jwt.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@AllArgsConstructor
public class AuthenticateController {  // Changed to public access
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final ApplicationUserDetailsService userDetailsService;


    @PostMapping("/authenticate")  // Specify HTTP method
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest req
    ) {
        try {
            // 1. Authenticate using Spring Security's AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
            final String jwt = jwtTokenUtil.generateToken(userDetails);

            // 4. Return the token in response
            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (BadCredentialsException e) {
            // Proper error handling with HTTP status code
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password", e);
        }
    }
}