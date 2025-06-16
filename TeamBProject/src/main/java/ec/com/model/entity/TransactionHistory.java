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

	// 🆕 講座ID追加！
	private Long lessonId;

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

	// lessonIdのgetter/setter追加！
	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
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

	// デフォルトコンストラクタ
	public TransactionHistory() {
	}

	// 🆕 lessonId入りのコンストラクタも追加！
	public TransactionHistory(Long userId, Long lessonId, Integer amount, Timestamp transactionDate) {
		this.userId = userId;
		this.lessonId = lessonId;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
}
