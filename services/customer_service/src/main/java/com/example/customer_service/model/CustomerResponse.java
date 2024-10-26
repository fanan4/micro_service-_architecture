package com.example.customer_service.model;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Adress address
) {
}
