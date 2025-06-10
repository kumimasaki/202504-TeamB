package ec.com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.model.dao.LessonDao;
import ec.com.model.entity.Lesson;

@Service
public class LessonService {
	@Autowired
	private LessonDao lessonDao;

	// Lesson Allのcheck
	// Lessonの情報を取得する時で使う
	public List<Lesson> selectAllLessonList(Long lessonId) {
		if (lessonId == null) {
			return null;
		} else {
			return lessonDao.findAll();
		}
	}
}
