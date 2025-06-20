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
    
    private Long amount;

    private Boolean isVisible =true;

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
    
    public Long getAmount(Long amount) {
        return amount;
    }
    
    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }


    // --- コンストラクタ（引数なし）---
    public TransactionItem() {}

    // --- コンストラクタ（引数あり）---
    public TransactionItem(Long lessonId, Long transactionId) {
        this.lessonId = lessonId;
        this.transactionId = transactionId;
    }
    
    

}
