package com.bengkel.booking.services;

import java.time.LocalTime;
import java.util.List;

import com.bengkel.booking.models.Customer;

public class Validation {

	public static Boolean isValidUser(List<Customer> customers, String id) {
		return UserService.getCustomerById(customers, id) != null;
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
