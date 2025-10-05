package repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import model.Account;

public class AccountRepository {

	private Map<String, Account> accounts = new HashMap<String, Account>();

	public Map<String, Account> getAccounts() {
		return Collections.unmodifiableMap(accounts);
	}

	public void save(Account account) {
		accounts.put(account.getAccountNumber(), account);
	}

	public Account findByAccountNumber(String accountNumber) {
		return accounts.get(accountNumber);

	}

	public void delete(String accountNumber) {
		accounts.remove(accountNumber);
	}

	public Map<String, Account> findAll() {
		return Collections.unmodifiableMap(accounts);
	}

	public void clear() {
		accounts.clear();
	}

}
