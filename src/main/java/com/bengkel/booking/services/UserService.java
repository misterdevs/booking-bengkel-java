package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.Customer;

public class UserService {

    public static Customer getCustomerById(List<Customer> customers, String id) {
        return customers.stream().filter(a -> a.getCustomerId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

}
