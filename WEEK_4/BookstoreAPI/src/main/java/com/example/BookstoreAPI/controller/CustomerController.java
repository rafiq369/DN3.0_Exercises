package com.example.BookstoreAPI.controller;

import com.example.BookstoreAPI.dto.CustomerDTO;
import com.example.BookstoreAPI.exception.ResourceNotFoundException;
import com.example.BookstoreAPI.mapper.CustomerMapper;
import com.example.BookstoreAPI.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private List<Customer> customers = new ArrayList<>();
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        customers.add(customer);
        CustomerDTO createdCustomerDTO = customerMapper.customerToCustomerDTO(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> registerCustomer(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String address) {

        Customer customer = new Customer();
        customer.setId(customers.size() + 1); // Simple auto-increment logic
        customer.setName(name);
        customer.setEmail(email);
        customer.setAddress(address);

        customers.add(customer);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

}
