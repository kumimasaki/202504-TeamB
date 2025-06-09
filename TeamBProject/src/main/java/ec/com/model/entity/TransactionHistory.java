package ec.com.model.entity;

import java.sql.Timestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionId;
	private Long userId;
	private Integer amount;
	private Timestamp transactionDate;
	
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Timestamp getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public TransactionHistory() {
	}
	public TransactionHistory(Long transactionId, Long userId, Integer amount, Timestamp transactionDate) {
		this.transactionId = transactionId;
		this.userId = userId;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
}
