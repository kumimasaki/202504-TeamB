package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.TransactionHistory;

@Repository
public interface TransactionHistoryDao extends JpaRepository<TransactionHistory, Long>{
	TransactionHistory save(TransactionHistory transactionHistory);
	List<TransactionHistory> findAll();
	TransactionHistoryDao findByTransactionId(Long transactionId);
}