package com.example.customer_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import com.example.customer_service.model.Customer;
import com.example.customer_service.service.CustomerService;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public String createCustomer(
            @RequestBody Customer customer) {

        return customerService.createCustomer(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomer(
            @PathVariable String id) {

        Map<String, AttributeValue> item =
                customerService.getCustomer(id);

        if (item == null || item.isEmpty()) {
            return null;
        }

        Customer customer = new Customer();

        customer.setCustomerId(
                item.get("CustomerId").s());

        customer.setName(
                item.get("Name").s());

        return customer;
    }

    @PutMapping("/{id}")
    public String updateCustomer(
            @PathVariable String id,
            @RequestBody Customer customer) {

        return customerService.updateCustomer(
                id,
                customer.getName());
    }
}