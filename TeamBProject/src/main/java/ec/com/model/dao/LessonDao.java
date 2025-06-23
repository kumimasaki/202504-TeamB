
package ec.com.model.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.Lesson;
import jakarta.transaction.Transactional;

@Repository
@Transactional
/*
 * 大事な物、データを丸ごと処理する saveやdeleteなどことに対して重要 全て消す、もしできないなら、そのまま置いて
 */
public interface LessonDao extends JpaRepository<Lesson, Long> {
	// 保存と更新
	Lesson save(Lesson lesson);

	// SELECT * FROM lesson
	// 講座全件を取得
	List<Lesson> findAll();

	// SELECT * FROM lesson WHERE lesson_id = ?
	// 講座IDを使って講座情報を取得する
	Lesson findByLessonId(Long lessonId);

	// 【新規追加】現在時刻以降の講座のみ取得（時分まで精密チェック）
	// 開始日時（日付+時刻）が現在時刻以降の講座を抽出
	@Query("SELECT l FROM Lesson l WHERE "
			+ "CAST(CONCAT(l.startDate, ' ', l.startTime) AS timestamp) >= :currentDateTime "
			+ "ORDER BY l.startDate, l.startTime")
	List<Lesson> findUpcomingLessons(@Param("currentDateTime") LocalDateTime currentDateTime);

	// キーワードで講座名を部分一致検索し、
	// さらに「開始日 > 指定日」または「開始日 = 指定日 かつ 開始時刻 >= 指定時刻」の条件に一致する講座を取得
	@Query("""
		    SELECT l FROM Lesson l 
		    WHERE 
		        (l.lessonName LIKE %:keyword% OR l.lessonDetail LIKE %:keyword%)
		        AND (l.startDate > :date OR (l.startDate = :date AND l.startTime >= :time))
		""")
		List<Lesson> findByLessonNameContainingAndDateTimeCondition(
		    @Param("keyword") String keyword,
		    @Param("date") LocalDate date,
		    @Param("time") LocalTime time
		);


	// DELETE FROM lesson WHERE lesson_id = ?
	// 削除使用
	// ここではデータを丸ごと処理する必要あり
	void deleteById(Long lessonId);

	// 講座名 or 講座番号 に keyword が含まれていて、
	@Query("SELECT l FROM Lesson l WHERE CAST(l.lessonId AS string) LIKE :keyword OR l.lessonName LIKE :keyword")
	List<Lesson> searchByKeyword(@Param("keyword") String keyword);
	
	@Query(value = "SELECT COUNT(*) FROM transaction_item t2 LEFT JOIN lesson t3 ON t2.lesson_id = t3.lesson_id WHERE t3.lesson_id = :lessonId", nativeQuery = true)
	Integer findLessonCountByLessonId(@Param("lessonId") Long lessonId);

}