package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.Lesson;

@Repository
public interface LessonDao extends JpaRepository<Lesson, Long>{
	Lesson save(Lesson lesson);
	List<Lesson> findAll();
	Lesson findByLessonId(Long lessonId);
}