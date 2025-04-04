package com.example.demo.antihero;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin(allowedHeaders = "Content-type")
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/customers")
@PreAuthorize("isAuthenticated()")
public class AntiHeroController {
    private final CustomerService service;
    private final ModelMapper mapper;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<CustomerDto> getAntiHeroes(Pageable pageable) {
        int toSkip = pageable.getPageSize() * pageable.getPageNumber();

        // Mapstruct is another dto mapper, but it's not straight forward
        var antiHeroList = StreamSupport
                .stream(service.findAllCustomer().spliterator(), false)
                .skip(toSkip).limit(pageable.getPageSize())
                .collect(Collectors.toList());


        return antiHeroList
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CustomerDto getAntiHeroById(@PathVariable("id") UUID id) {
        return convertToDto(service.findCustomerById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteAntiHeroById(@PathVariable("id") UUID id) {
        service.removeCustomerById(id);
    }

    @PostMapping
    public CustomerDto postAntiHero(@Valid @RequestBody CustomerDto customerDto) {
        var entity = convertToEntity(customerDto);
        var customer = service.addCustomer(entity);

        return convertToDto(customer);
    }

    @PutMapping("/{id}")
    public void putAntiHero(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CustomerDto customerDto
    ) {
        if (!id.equals(customerDto.getId())) throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "id does not match"
        );

        var antiHeroEntity = convertToEntity(customerDto);
        service.updateAntiHero(id, antiHeroEntity);
    }

    private CustomerDto convertToDto(CustomerEntity entity) {
        return mapper.map(entity, CustomerDto.class);
    }

    private CustomerEntity convertToEntity(CustomerDto dto) {
        return mapper.map(dto, CustomerEntity.class);
    }

}