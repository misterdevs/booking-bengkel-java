package com.bengkel.booking.services;

import java.util.List;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class CustomerService {

    public static void topupSaldo(MemberCustomer customer, double value) {
        customer.setSaldoCoin(customer.getSaldoCoin() + value);
    }

    public static Customer getCustomerById(List<Customer> customers, String id) {
        return customers.stream().filter(a -> a.getCustomerId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static List<Vehicle> getVehicleCustomer(Customer customer) {
        return customer.getVehicles();
    }

    public static Vehicle findVehicleCustomerByVehicleId(List<Vehicle> vehicles, String id) {
        return vehicles.stream().filter(a -> a.getVehiclesId().equals(id)).findFirst().orElse(null);
    }

}
