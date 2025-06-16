package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.com.model.entity.Lesson;
import ec.com.model.entity.TransactionHistory;

@Repository
public interface TransactionHistoryDao extends JpaRepository<TransactionHistory, Long> {
	TransactionHistory save(TransactionHistory transactionHistory);

	List<TransactionHistory> findAll();

	List<TransactionHistory> findByUserId(Long UserId);

	TransactionHistoryDao findByTransactionId(Long transactionId);

	@Query(value = """
			SELECT
			  l.lesson_id,
			  l.lesson_name,
			  l.lesson_detail,
			  l.image_name,
			  l.start_date,
			  l.start_time,
			  l.finish_time,
			  l.lesson_fee,
			  th.transaction_date,
			  th.transaction_id,
			  ti.id
			FROM transaction_history th
			JOIN transaction_item ti ON th.transaction_id = ti.transaction_id
			JOIN lesson l ON l.lesson_id = ti.lesson_id
			WHERE th.user_id = :userId
			""", nativeQuery = true)
	List<Object[]> findLessonAndTransactionByUserId(@Param("userId") Long userId);
//	
//	@Query(value = """
//			SELECT
//			  l.lesson_id,
//			  l.lesson_name,
//			  l.lesson_detail,
//			  l.image_name,
//			  l.start_date,
//			  l.start_time,
//			  l.finish_time,
//			  l.lesson_fee,
//			  th.transaction_date,
//			  th.transaction_id,
//			  ti.id
//			FROM transaction_history th
//			JOIN transaction_item ti ON th.transaction_id = ti.transaction_id
//			JOIN lesson l ON l.lesson_id = ti.lesson_id
//			WHERE th.user_id = :userId
//			""", nativeQuery = true)
//	List<Object[]> findLessonAndTransactionByUserIdAndBuyTime(@Param("userId") Long userId,Long userId);
}