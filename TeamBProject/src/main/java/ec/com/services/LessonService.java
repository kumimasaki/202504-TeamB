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

    // すべての講座を取得する
    public List<Lesson> findAll() {
        return lessonDao.findAll();
    }

    // 新しい講座を登録する
    public void registerLesson(Lesson lesson) {
        lessonDao.save(lesson);
    }

    // IDで講座を検索
    public Lesson findById(Long lessonId) {
        return lessonDao.findById(lessonId).orElse(null);
    }

    // 管理者IDに紐づく講座をすべて取得
    public List<Lesson> selectAllLessonList(Long adminId) {
        return lessonDao.findByAdminId(adminId);
    }
}
