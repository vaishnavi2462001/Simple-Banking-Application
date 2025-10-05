package service;

import java.math.BigDecimal;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import exception.AccountNotFoundException;
import exception.DuplicateAccountException;
import model.Account;
import model.Customer;
import repository.AccountRepository;
import repository.TransactionRepository;

public class AccountServiceTest {

	private AccountService accountservice;
	private TransactionService transervice;
	private AccountRepository accountrepo;
	private TransactionRepository transrepo;
	private Customer data;

	@BeforeClass
	public void classSetup() {
		accountrepo = new AccountRepository();
		transrepo = new TransactionRepository();
		transervice = new TransactionService(accountrepo, transrepo);
	}

	@BeforeMethod
	public void setup() {
		accountrepo = new AccountRepository();
		accountservice = new AccountService(accountrepo);
		transrepo = new TransactionRepository();
		transervice = new TransactionService(accountrepo, transrepo);
		CustomerService customer = new CustomerService();
		data = customer.addcustomer("qwerty", "qwerty@gmail.com", "1234567899");
	}

	@Test(groups = { "account" }, priority = 1)
	public void addAccount() throws DuplicateAccountException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountservice.addAccount(account);
		Assert.assertEquals(accountrepo.findByAccountNumber(account.getAccountNumber()), account,
				"Account is not added successfully");
	}

	@Test(groups = { "account" }, priority = 2, dependsOnMethods = { "addAccount" })
	public void getBalance() throws DuplicateAccountException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("500.00"), Account.AccountType.SAVINGS);
		accountservice.addAccount(account);
		BigDecimal balance = accountservice.getBalance(account.getAccountNumber());
		Assert.assertEquals(balance, new BigDecimal("500.00"), "Balance retrieved is incorrect");
	}

	@Test(groups = { "account" }, priority = 3, dependsOnMethods = { "addAccount" })
	public void deleteAccount() throws DuplicateAccountException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.CHECKING);
		accountservice.addAccount(account);
		accountservice.deleteAccount(account.getAccountNumber());
		Account deleted = accountrepo.findByAccountNumber(account.getAccountNumber());
		Assert.assertNull(deleted, "Account not deleted successfully!");
	}

	@Test(groups = { "account" }, priority = 4)
	public void getAllAccounts() throws DuplicateAccountException {
		accountservice
				.addAccount(new Account(data.getCustomerId(), new BigDecimal("808.00"), Account.AccountType.SAVINGS));
		accountservice
				.addAccount(new Account(data.getCustomerId(), new BigDecimal("977.00"), Account.AccountType.CHECKING));
		Map<String, Account> allAccounts = accountrepo.findAll();
		Assert.assertTrue(allAccounts.size() >= 2, "Should have returned all saved accounts");
	}

	@Test(groups = { "transaction" }, expectedExceptions = AccountNotFoundException.class, priority = 5)
	public void checkAccountNotFoundException() throws Exception {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.00"), Account.AccountType.SAVINGS);
		accountrepo.save(account);

		Account dummyAccount = new Account("qwerty@1234523", new BigDecimal("0.00"), Account.AccountType.SAVINGS);

		transervice.transfer(account, dummyAccount, new BigDecimal("100"));
	}

	@AfterMethod
	public void cleanup() {

		accountrepo.clear();
		transrepo.clear();
	}

}
