package com.example.customerportal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    private final List<Customer> customers = new ArrayList<>();

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Customer Portal is running");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"healthy\"}");
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> registerCustomer(
            @RequestBody Customer customer) {

        customer.setId(customers.size() + 1);
        customers.add(customer);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customer);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(
            @PathVariable int id) {

        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return ResponseEntity.ok(customer);
            }
        }

        return ResponseEntity.notFound().build();
    }
}