package service;

import java.util.HashMap;
import java.util.Map;

import model.Customer;

public class CustomerService {

	private Map<String, Customer> customers = new HashMap<>();

	private void validateCustomerData(String name, String email, String phoneNumber) {
		if (name == null || !name.matches("[A-Za-z\\s'-]+")) {
			throw new IllegalArgumentException("Invalid Name entered");
		}
		if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			throw new IllegalArgumentException("Invalid Email entered");
		}
		if (phoneNumber == null || !phoneNumber.matches("\\d{10,}")) {
			throw new IllegalArgumentException("Invalid Phone Number entered");
		}
	}

	public Customer addcustomer(String name, String email, String phoneNumber) {
		validateCustomerData(name, email, phoneNumber);
		Customer customer = new Customer(name, email, phoneNumber);
		customers.put(customer.getCustomerId(), customer);
		return customer;
	}

	public Customer findCustomerById(String customerId) {
		return customers.get(customerId);
	}

}
