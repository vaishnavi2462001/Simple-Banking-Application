package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
	public enum TransactionType {
		DEPOSIT, WITHDRAWAL, TRANSFER
	};

	private String transactionId, accountNumber;
	private TransactionType type;
	private BigDecimal amount;
	private LocalDateTime timestamp;
	private Account relatedAccount;

	public Transaction(TransactionType type, String accountNumber, BigDecimal amount, LocalDateTime timestamp,
			Account relatedAccount) {
		this.type = type;
		this.transactionId = generateNewId();
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.timestamp = timestamp;
		this.relatedAccount = relatedAccount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Account getRelatedAccount() {
		return relatedAccount;
	}

	public void setRelatedAccount(Account relatedAccount) {
		this.relatedAccount = relatedAccount;
	}

	public static String generateNewId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public String toString() {
		return "Transaction [TransactionID = " + transactionId + ", AccountNumber = " + accountNumber
				+ ", Account Type = " + type + ", Amount = " + amount + ", Timestamp = " + timestamp
				+ ", Related Account = " + relatedAccount + "]";
	}

}
