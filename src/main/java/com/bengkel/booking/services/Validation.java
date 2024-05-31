package com.bengkel.booking.services;

import java.time.LocalTime;
import java.util.List;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.MemberCustomer;

public class Validation {

	public static Boolean isValidCustomer(List<Customer> customers, String id) {
		return CustomerService.getCustomerById(customers, id) != null;
	}

	public static Boolean isValidTransaction(Customer customer, String paymentMethod) {
		return paymentMethod == "Saldo Koin" ? isMember(customer) : isMember(customer);

	}

	public static Boolean isMember(Customer customer) {
		return customer instanceof MemberCustomer;
	}

	public static Boolean isValidAuth(Customer customer, String password) {
		return customer.getPassword().equals(password);
	}

	public static Boolean isReachedLimitAttemptsAuth(int numbOfAttempts) {
		int limitAttempts = 3;
		return numbOfAttempts >= limitAttempts;
	}

	public static boolean isSuspendedCustomer(Object[] suspendedCustomer) {
		return suspendedCustomer != null && LocalTime.now().compareTo(((LocalTime) suspendedCustomer[1])) < 0;
	}
}
