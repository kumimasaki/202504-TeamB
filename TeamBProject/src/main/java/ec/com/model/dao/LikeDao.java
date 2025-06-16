package ec.com.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ec.com.model.entity.Like;
import ec.com.model.entity.TransactionItem;
import jakarta.transaction.Transactional;

@Repository
public interface LikeDao extends JpaRepository<Like, Long>{
	Like save(Like like);
	List<Like> findAll();
	List<Like> findByUserId(Long userId);
	Like findByLessonIdAndUserId(Long lessonId, Long userId);
	
	@Transactional
    void deleteByLessonIdAndUserId(Long lessonId, Long userId);
	
	//Like数をカウントしてランキングにする機能
	@Query("SELECT l.lessonId, COUNT(l.id) "
			+ "FROM Like l GROUP BY l.lessonId "
			+ "ORDER BY COUNT(l.id) DESC")
	List<Object[]> getLessonLikeRanking();
}