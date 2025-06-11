package ec.com.model.dao;
import java.time.LocalDate;  // 【新增】添加这个import
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.Lesson;

@Repository
public interface LessonDao extends JpaRepository<Lesson, Long>{
	Lesson save(Lesson lesson);
	List<Lesson> findAll();
	Lesson findByLessonId(Long lessonId);
	// 管理者IDに紐づく講座をすべて取得
	List<Lesson> findByAdminId(Long adminId);
	
	// 【新規追加】当日以降の講座のみ取得
	List<Lesson> findByStartDateGreaterThanEqual(LocalDate currentDate);
}
