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

    // --- コンストラクタ（引数なし）---
    public TransactionItem() {}

    // --- コンストラクタ（引数あり）---
    public TransactionItem(Long lessonId, Long transactionId) {
        this.lessonId = lessonId;
        this.transactionId = transactionId;
    }

}
