package ec.com.model.dao;

import java.time.LocalDateTime;
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

	// SELECT * FROM lesson WHERE admin_id = ?
	// 管理者IDに紐づく講座をすべて取得
	List<Lesson> findByAdminId(Long adminId);

	// 【新規追加】現在時刻以降の講座のみ取得（時分まで精密チェック）
	// 開始日時（日付+時刻）が現在時刻以降の講座を抽出
	@Query("SELECT l FROM Lesson l WHERE "
			+ "CAST(CONCAT(l.startDate, ' ', l.startTime) AS timestamp) >= :currentDateTime "
			+ "ORDER BY l.startDate, l.startTime")
	List<Lesson> findUpcomingLessons(@Param("currentDateTime") LocalDateTime currentDateTime);

	// DELETE FROM lesson WHERE lesson_id = ?
	// 削除使用
	// ここではデータを丸ごと処理する必要あり
	void deleteById(Long lessonId);

}