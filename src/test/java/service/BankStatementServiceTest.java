package service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import model.Account;
import model.Customer;
import model.Transaction;
import model.Transaction.TransactionType;
import repository.AccountRepository;
import repository.TransactionRepository;

public class BankStatementServiceTest {
	
//	TransactionRepository transrepo=new TransactionRepository();
//	BankStatementService bank=new BankStatementService(transrepo);
//	AccountRepository accountrepo=new AccountRepository();
//	AccountService acccountservice=new AccountService(accountrepo);
	
	private TransactionRepository transrepo;
    private AccountService acccountservice;
    private BankStatementService bankservice;
    private AccountRepository accountrepo;
    private Customer data;

	
	@BeforeMethod
	public void setup() {
		accountrepo = new AccountRepository();
	    acccountservice = new AccountService(accountrepo);
	    transrepo = new TransactionRepository();
	    bankservice = new BankStatementService(transrepo);
		CustomerService customer=new CustomerService();
		data=customer.addcustomer("qwerty", "qwerty@gmail.com", "1234567899");
		

	}
	
	
	@Test(groups = {"statement"})
	public void checkStatement() {
		Account account=new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		
		Transaction transaction = new Transaction(TransactionType.DEPOSIT, account.getAccountNumber(), new BigDecimal("300.70"), LocalDateTime.now(), account);
		transrepo.save(transaction);
		    
		List <Transaction> tlist= bankservice.getStatements(account.getAccountNumber(), LocalDateTime.now().minusMonths(1), LocalDateTime.now());
		System.out.println(transrepo.findAll());
		
		Assert.assertEquals(tlist.size(),1,"Bank Statement not retreived");
	}
	
	@AfterMethod
	public void cleanup() {
	    
	    accountrepo.clear();  
	    transrepo.clear();
	}

}
