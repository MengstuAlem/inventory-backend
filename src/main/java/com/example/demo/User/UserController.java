package com.example.demo.User;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Log4j2

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/v1/users")
    public Iterable<UserDto> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/api/v1/users/{id}")
    public UserDto getUserById(@PathVariable("id") UUID id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/api/v1/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") UUID id) {
        userService.removeUserById(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Valid @RequestBody UserDto userDto)
            throws NoSuchAlgorithmException, BadRequestException {
        return userService.createUser(userDto, userDto.getPassword());
    }

    @PutMapping("/api/v1/users/{id}")
    public void putUser(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UserDto userDto
    ) throws NoSuchAlgorithmException {
        userService.updateUser(id, userDto, userDto.getPassword());
    }
}