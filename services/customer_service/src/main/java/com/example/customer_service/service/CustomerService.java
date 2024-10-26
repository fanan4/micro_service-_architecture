package com.example.customer_service.service;


import com.example.customer_service.dao.CustomerRepository;
import com.example.customer_service.exception.CustomerNotFoundException;
import com.example.customer_service.mapper.CustomerMapper;
import com.example.customer_service.model.Customer;
import com.example.customer_service.model.CustomerRequest;
import com.example.customer_service.model.CustomerResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    public Customer addCoustomer(CustomerRequest request){
     Customer newCustomer=customerMapper.toCustomer(request);
     return customerRepository.save(newCustomer);
    }


    public void updateCustomer(CustomerRequest request) {
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer:: No customer found with the provided ID: %s", request.id())
                ));
        mergeCustomer(customer, request);
        customerRepository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return  customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public CustomerResponse findById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
    }

    public boolean existsById(String id) {
        return customerRepository.findById(id)
                .isPresent();
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }


}
