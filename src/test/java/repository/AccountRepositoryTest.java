package repository;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import model.Account;
import model.Customer;
import service.CustomerService;

public class AccountRepositoryTest {
	
	private AccountRepository accountrepo;
	private Customer data;
	
	
	@BeforeMethod
	public void setup() {
		accountrepo.clear();//makes sure repo gets cleared before every test so that it does not save prev accounts and while runninf findall test case, it does not match list size
		CustomerService customer=new CustomerService();
		data=customer.addcustomer("qwerty", "qwerty@gmail.com", "1234567899");
		
	}
	
	@Test
	public void checkSave() {
		Account account=new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		Assert.assertEquals(account, accountrepo.findByAccountNumber(account.getAccountNumber()));

	}

	
	@Test
	public void checkfindByAccountNumber() {
		Account account=new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		//System.out.println(accountrepo.findByAccountNumber(account.getAccountNumber()));
		Assert.assertEquals(account, accountrepo.findByAccountNumber(account.getAccountNumber()));
		
	}
	
	@Test
	public void checkDeletion() {
		Account account=new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		accountrepo.delete(account.getAccountNumber());
		//System.out.println(accountrepo.getAccounts().get(account.getAccountNumber()));
		Assert.assertNull(accountrepo.findByAccountNumber(account.getAccountNumber()), "Account was not deleted successfully!!");
		
	}
	
	@Test
	public void getAllAccounts() {
		List<Account> list=new ArrayList<>();
		Account account1=new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account1);
		list.add(account1);
		Account account2=new Account(data.getCustomerId(), new BigDecimal("1000.08"), Account.AccountType.CHECKING);
		accountrepo.save(account2);
		list.add(account2);
		Account account3=new Account(data.getCustomerId(), new BigDecimal("900.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account3);
		list.add(account3);
		Assert.assertEquals(accountrepo.findAll().size(), list.size(),"All Accounts were not successfully added");
	}
	
	

}
