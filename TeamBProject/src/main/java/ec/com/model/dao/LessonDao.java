package ec.com.model.dao;

import java.time.LocalDateTime; 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;
import ec.com.model.entity.Lesson;

@Repository
public interface LessonDao extends JpaRepository<Lesson, Long>{
    Lesson save(Lesson lesson);
    List<Lesson> findAll();
    Lesson findByLessonId(Long lessonId);
    // 管理者IDに紐づく講座をすべて取得
    List<Lesson> findByAdminId(Long adminId);
    
    //【新規追加】現在時刻以降の講座のみ取得（時分まで精密チェック）
    //開始日時（日付+時刻）が現在時刻以降の講座を抽出
    @Query("SELECT l FROM Lesson l WHERE " +
           "CAST(CONCAT(l.startDate, ' ', l.startTime) AS timestamp) >= :currentDateTime " +
           "ORDER BY l.startDate, l.startTime")
    List<Lesson> findUpcomingLessons(@Param("currentDateTime") LocalDateTime currentDateTime);
}