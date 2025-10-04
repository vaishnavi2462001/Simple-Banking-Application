package model;

import java.util.UUID;

public class Customer {

    public Customer(String name, String email, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.customerId = generateCustomerId();
        
	}

	private String name,email,phoneNumber,customerId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCustomerId() {
		return customerId;
	}

//	public void setCustomerId(String customerId) {
//		this.customerId = customerId;
//	}
	public static String generateCustomerId() {
        return UUID.randomUUID().toString();
    }

	@Override
	public String toString() {
		return "Customer [Name = " + name + ", Email = " + email + ", PhoneNumber = " + phoneNumber + ", CustomerID = "
				+ customerId + "]";
	}


}
