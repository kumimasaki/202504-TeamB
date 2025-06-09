package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.TransactionItem;

@Repository
public interface TransactionItemDao extends JpaRepository<TransactionItem, Long>{
	TransactionItem save(TransactionItem transactionItem);
	List<TransactionItem> findAll();
	TransactionItem findByLessonId(Long lessonId);
}