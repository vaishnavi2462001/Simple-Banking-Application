package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Transaction;

public class TransactionRepository {
	private List<Transaction> transactions = new ArrayList<Transaction>();

	public void save(Transaction transaction) {
		transactions.add(transaction);

	}

	public List<Transaction> findAll() {
		return Collections.unmodifiableList(transactions);
	}

	public void clear() {
		transactions.clear();

	}

}
