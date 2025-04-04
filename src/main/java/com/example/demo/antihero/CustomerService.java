package com.example.demo.antihero;
import java.util.UUID;

import com.example.demo.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository repo;

    public Iterable<CustomerEntity> findAllCustomer() {
        return repo.findAll();
    }

    public CustomerEntity findCustomerById(UUID id) {
        return findOrThrow(id);
    }

    public void removeCustomerById(UUID id) {
        repo.deleteById(id);
    }

    public CustomerEntity addCustomer(CustomerEntity customer) {
        return repo.save(customer);
    }

    public void updateAntiHero(UUID id, CustomerEntity customer) {
        findOrThrow(id);
        repo.save(customer);
    }

    private CustomerEntity findOrThrow(final UUID id) {
        return repo
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Anti-hero by id " + id + " was not found")
                );
    }
}
