package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.TransactionItem;

@Repository
public interface TransactionItemDao extends JpaRepository<TransactionItem, Long>{
	TransactionItem save(TransactionItem transactionItem);
	List<TransactionItem> findAll();
	TransactionItem findByLessonId(Long lessonId);
	  @Modifying
	    @Query(
	    	"DELETE FROM TransactionItem ti "
	    	+ "WHERE ti.transactionId = :transactionId")
	    void deleteByTransactionId(@Param("transactionId") Long transactionId);
}