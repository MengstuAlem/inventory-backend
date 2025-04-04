package com.example.demo.antihero;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;


@Data
public class CustomerDto {

    private UUID id;

    @NotNull(message = "First Name is required")
    private String firstName;

    private String lastName;
    private String house;
    private String knownAs;
}
