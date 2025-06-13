package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
}