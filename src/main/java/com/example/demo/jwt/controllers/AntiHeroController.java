package com.example.demo.jwt.controllers;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("isAuthenticated()")
public class AntiHeroController {
}
