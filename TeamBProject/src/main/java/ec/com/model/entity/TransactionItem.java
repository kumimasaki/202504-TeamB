package ec.com.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private Long lessonId;

    private Long transactionId;

    @Column(name = "amount") // ：金額（価格）
    private Integer amount;

    // --- Getter / Setter ---
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    // --- コンストラクタ（引数なし）---
    public TransactionItem() {}

    // --- コンストラクタ（引数あり）---
    public TransactionItem(Long lessonId, Long transactionId) {
        this.lessonId = lessonId;
        this.transactionId = transactionId;
    }

    // 🆕 オプション：amountまで含むコンストラクタも追加したいなら下記もOK
    public TransactionItem(Long lessonId, Long transactionId, Integer amount) {
        this.lessonId = lessonId;
        this.transactionId = transactionId;
        this.amount = amount;
    }
}
