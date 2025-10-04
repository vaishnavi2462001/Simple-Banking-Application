package service;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Customer;
import repository.AccountRepository;
import repository.TransactionRepository;

public class TransactionServiceTest {

	private TransactionRepository transrepo;
	private AccountRepository accountrepo;
	private TransactionService transervice;
	private Customer data;

	@BeforeClass
	public void classSetup() {
		accountrepo = new AccountRepository();
		transrepo = new TransactionRepository();
		transervice = new TransactionService(accountrepo, transrepo);
	}

	@BeforeMethod
	public void setup() {
		CustomerService customer = new CustomerService();
		data = customer.addcustomer("qwerty", "qwerty@gmail", "1234567899");
	}

	@Test(groups = { "transaction" }, priority = 1)
	public void checkDeposit() throws AccountNotFoundException, InvalidAmountException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		transervice.deposit(account.getAccountNumber(), new BigDecimal("200"));
		Assert.assertEquals(account.getBalance(), new BigDecimal("300.08"), "Balance after deposit is incorrect");
	}

	@Test(groups = { "withdraw" }, priority = 2, dependsOnMethods = { "checkDeposit" })
	public void checkwithdraw() throws InsufficientFundsException, AccountNotFoundException, InvalidAmountException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account);

		BigDecimal withdrawAmount = new BigDecimal("50.55");
		transervice.withdraw(account.getAccountNumber(), withdrawAmount);

		BigDecimal expectedBalance = new BigDecimal("49.53"); // 100.08 - 50.55 = 49.53
		Assert.assertEquals(account.getBalance(), expectedBalance, "Balance after withdrawal is incorrect");
	}

	@Test(groups = { "transaction" }, priority = 3, dependsOnMethods = { "checkDeposit" })
	public void checktransfer() throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
		Account account1 = new Account(data.getCustomerId(), new BigDecimal("100.08"), Account.AccountType.SAVINGS);
		accountrepo.save(account1);

		Account account2 = new Account(data.getCustomerId(), new BigDecimal("200.08"), Account.AccountType.CHECKING);
		accountrepo.save(account2);

		BigDecimal expectedacc1balance = account1.getBalance().subtract(new BigDecimal("10.08")); // 100.08 - 10.08
		BigDecimal expectedacc2balance = account2.getBalance().add(new BigDecimal("10.08")); // 200.08 + 10.08

		transervice.transfer(account1, account2, new BigDecimal("10.08"));

		//Assert.assertEquals(account1.getBalance(), expectedacc1balance, "Sender balance after transfer is incorrect");
		Assert.assertTrue(account1.getBalance().compareTo(expectedacc1balance) == 0, "Sender balance after transfer is incorrect");

		Assert.assertEquals(account2.getBalance(), expectedacc2balance, "Receiver balance after transfer is incorrect");
	}

	@Test(expectedExceptions = AccountNotFoundException.class)
	public void accountNotFound() throws AccountNotFoundException, InvalidAmountException {
		transervice.deposit("jyawfgukseghzoldjtgpkj", new BigDecimal("100"));
	}

	@Test(expectedExceptions = InsufficientFundsException.class)
	public void checkingInsufficientFundsException() throws Exception {
		Account account = new Account(data.getCustomerId(), new BigDecimal("50.00"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		transervice.withdraw(account.getAccountNumber(), new BigDecimal("100.00"));
	}

	@Test(expectedExceptions = InvalidAmountException.class)
	public void checkInvalidAmountException() throws Exception {
		Account account = new Account(data.getCustomerId(), new BigDecimal("1000.00"), Account.AccountType.SAVINGS);
		accountrepo.save(account);
		BigDecimal invalidAmount = new BigDecimal("0");// 0 or -ve amount
		transervice.deposit(account.getAccountNumber(), invalidAmount);
	}

	@Test(expectedExceptions = AccountNotFoundException.class)
	public void checkAccountNotFoundException() throws Exception {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.00"), Account.AccountType.SAVINGS);
		accountrepo.save(account);

		Account dummyAccount = new Account("qwerty@1234523", new BigDecimal("0.00"), Account.AccountType.SAVINGS);

		transervice.transfer(account, dummyAccount, new BigDecimal("100"));
	}

	@Test(dataProvider = "depositamounts", priority = 4, dependsOnMethods = { "checkDeposit" })
	public void checkdepositUsingDP(BigDecimal amount, boolean customException)
			throws AccountNotFoundException, InvalidAmountException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("100.00"), Account.AccountType.SAVINGS);
		accountrepo.save(account);

		try {
			transervice.deposit(account.getAccountNumber(), amount);
			if (customException) {
				Assert.fail("Expected InvalidAmountException for amount: " + amount);
			}
		} catch (InvalidAmountException e) {
			if (!customException) {
				Assert.fail("Did not expect InvalidAmountException for amount: " + amount);
			}
			Assert.assertTrue(e.getMessage().contains("Invalid"), "Unexpected exception message: " + e.getMessage());
		}
	}

	@Test(dataProvider = "withdrawamounts", priority = 5, dependsOnMethods = { "checkwithdraw" })
	public void checkwithdrawUsingDP(BigDecimal amount, boolean customException)
			throws InsufficientFundsException, AccountNotFoundException {
		Account account = new Account(data.getCustomerId(), new BigDecimal("200.00"), Account.AccountType.SAVINGS);
		accountrepo.save(account);

		try {
			transervice.withdraw(account.getAccountNumber(), amount);
			if (customException) {
				Assert.fail("Expected InvalidAmountException for withdrawal amount: " + amount);
			}
			BigDecimal expectedBalance = new BigDecimal("200.00").subtract(amount);
			Assert.assertEquals(account.getBalance(), expectedBalance, "Balance after withdrawal is incorrect");
		} catch (InvalidAmountException e) {
			if (!customException) {
				Assert.fail("Did not expect InvalidAmountException for amount: " + amount);
			}
			Assert.assertTrue(e.getMessage().contains("Invalid"), "Unexpected exception message: " + e.getMessage());
		}
	}

	@Test(dataProvider = "transferamounts", priority = 6, dependsOnMethods = { "checktransfer" })
	public void checktransferUsingDP(BigDecimal amount, boolean customException) throws AccountNotFoundException {
		Account sender = new Account(data.getCustomerId(), new BigDecimal("200.00"), Account.AccountType.SAVINGS);
		accountrepo.save(sender);
		Account receiver = new Account(data.getCustomerId(), new BigDecimal("100.00"), Account.AccountType.CHECKING);
		accountrepo.save(receiver);

		try {
			transervice.transfer(sender, receiver, amount);
			if (customException) {
				Assert.fail("Expected exception for transfer amount: " + amount);
			}
			BigDecimal expectedSenderBalance = new BigDecimal("200.00").subtract(amount);
			BigDecimal expectedReceiverBalance = new BigDecimal("100.00").add(amount);

			Assert.assertEquals(sender.getBalance(), expectedSenderBalance, "Sender balance incorrect after transfer");
			Assert.assertEquals(receiver.getBalance(), expectedReceiverBalance,
					"Receiver balance incorrect after transfer");

		} catch (InvalidAmountException | InsufficientFundsException e) {
			if (!customException) {
				Assert.fail("Unexpected exception for transfer amount: " + amount + ". Exception: " + e);
			}
			Assert.assertTrue(e.getMessage().contains("Invalid") || e.getMessage().contains("Insufficient"),
					"Unexpected exception message: " + e.getMessage());
		}
	}
	
	
	 @Test(expectedExceptions = InvalidAmountException.class, dependsOnMethods = { "checkDeposit" })
	    public void checkSameAccountTransfer()
	            throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
	        Account account = new Account(data.getCustomerId(), new BigDecimal("1000.00"), Account.AccountType.SAVINGS);
	        accountrepo.save(account);
	        transervice.transfer(account, account, new BigDecimal("100.00"));
	 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@AfterMethod
	public void cleanup() {

		accountrepo.clear();
		transrepo.clear();
	}

	@DataProvider(name = "depositamounts")
	public Object[][] depositAmounts() {
	    return new Object[][] {
	        { new BigDecimal("100.00"), false },   // valid deposit
	        { new BigDecimal("0.00"), true },      // zero amount - expect exception
	        { new BigDecimal("-50.00"), true },    // negative amount - expect exception
	        { new BigDecimal("999999999999999999.99"), false }, // large valid deposit
	        { new BigDecimal("0.01"), false },     // smallest positive deposit
	        { new BigDecimal("0.0000000001"), false } // very small positive deposit
	    };
	}
	
	@DataProvider(name = "withdrawamounts")
	public Object[][] withdrawAmounts() {
	    return new Object[][] {
	        { new BigDecimal("50.00"), false },   // valid withdraw
	        { new BigDecimal("0.01"), false },    // smallest positive
	        { new BigDecimal("0.00"), true },     // zero - invalid
	        { new BigDecimal("-20.00"), true },   // negative - invalid
	        { new BigDecimal("999999999999999999.99"), true }, // large amount exceeding balance
	        { new BigDecimal("100.00"), false }   // exact balance if initial balance is 100
	    };
	}
	
	@DataProvider(name = "transferamounts")
	public Object[][] transferAmounts() {
	    return new Object[][] {
	        { new BigDecimal("100.00"), false },  // valid transfer within balance
	        { new BigDecimal("0.01"), false },    // smallest positive transfer
	        { new BigDecimal("0.00"), true },     // zero transfer - expect exception
	        { new BigDecimal("-50.00"), true },   // negative transfer - expect exception
	        { new BigDecimal("999999999999999999.99"), true }, // large transfer expected to fail
	        { new BigDecimal("50.00"), false }    // valid transfer
	    };
	}


}