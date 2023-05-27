package com.guilherme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/customers")
public class Main {
    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setAge(request.age());
        customer.setEmail(request.email());
        customer.setName(request.name());
        customerRepository.save(customer);
    }

    record NewCustomerRequest(
           String name,
           String email,
           Integer age
    ) {}

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id) { customerRepository.deleteById(id); }

    @PutMapping("{customerId}")
    public void updateCustomer(
                @PathVariable("customerId") Integer id,
                @RequestBody NewCustomerRequest request
    ) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            Customer updatedCustomer = customer.get();
            updatedCustomer.setAge(request.age());
            updatedCustomer.setEmail(request.email());
            updatedCustomer.setName(request.name());
            customerRepository.save(updatedCustomer);
        }
    }

    record UpdateCustomerRequest(
            String name,
            String email,
            Integer age
    ) {}
}
