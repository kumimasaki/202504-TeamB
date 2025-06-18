package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.TransactionItem;

@Repository
public interface TransactionItemDao extends JpaRepository<TransactionItem, Long> {
	TransactionItem save(TransactionItem transactionItem);

	List<TransactionItem> findAll();

	TransactionItem findByLessonId(Long lessonId);

	List<TransactionItem> findByTransactionId(Long transactionId);

	@Modifying
	@Query("DELETE FROM TransactionItem ti WHERE ti.transactionId = :transactionId")
	void deleteByTransactionId(@Param("transactionId") Long transactionId);

	// 購入履歴削除
	@Modifying
	@Query("DELETE FROM TransactionItem ti WHERE ti.id = :id")
	void deleteById(@Param("id") Long id);

	// 同じtransactionIdを持つitemがあるか判定
	boolean existsByTransactionId(Long transactionId);
	
	//lessonIDに紐づく取引アイテムの存在チェックメソッドを追加
	boolean existsByLessonId(Long lessonId);
	
	//購入済みレッスンカウント用
	long countByLessonId(Long lessonId);
}
