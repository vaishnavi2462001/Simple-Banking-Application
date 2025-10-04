package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;



public class Account {
	public enum AccountType {
	    SAVINGS,CHECKING
	} 
	private String accountNumber, customerId;
	private BigDecimal balance;
	private AccountType accountType;
	private LocalDateTime dateOpened;
	
	public Account(String customerId, BigDecimal balance, AccountType accountType) {
		this.accountNumber=generateAccountId();
		this.customerId = customerId;
		this.balance = balance;
		this.accountType = accountType;
		this.dateOpened = LocalDateTime.now();
		
	}
	@Override
	public String toString() {
		return "Account [AccountNumber=" + accountNumber + ", CustomerID=" + customerId + ", Balance=" + balance
				+ ", AccountType=" + accountType + ", AccountOpenedDate=" + dateOpened + "]";
	}
	public String getAccountNumber() {
		return accountNumber;
	}
//	public void setAccountNumber(String accountNumber) {
//		this.accountNumber = accountNumber;
//	} Removed public setter for accountNumber to prevent modification after creation
	public String getCustomerId() {
		return customerId;
	}
//	public void setCustomerId(String customerId) {
//		this.customerId = customerId;
//	}Removed public setter for customerId to preserve immutability
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	public LocalDateTime getDateOpened() {
		return dateOpened;
	}
	public void setDateOpened(LocalDateTime dateOpened) {
		this.dateOpened = dateOpened;
	}
	public static String generateAccountId() {
        return UUID.randomUUID().toString();
    }
//	public void setAccountNumber(String accountNumber2) {
//		this.accountNumber = accountNumber;
//		
//	}

}
