package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import model.Transaction;
import repository.TransactionRepository;

public class BankStatementService {

	private final TransactionRepository transrepo;

	public BankStatementService(TransactionRepository transrepo) {
		this.transrepo = transrepo;
	}

	public List<Transaction> getStatements(String accountNumber, LocalDateTime startdate, LocalDateTime enddate) {
		List<Transaction> tlist = transrepo.findAll();
		return tlist.stream().filter(i -> i.getAccountNumber().equals(accountNumber))
				.filter(i -> (!i.getTimestamp().isBefore(startdate) && !i.getTimestamp().isAfter(enddate)))
				.collect(Collectors.toList());

	}

}
