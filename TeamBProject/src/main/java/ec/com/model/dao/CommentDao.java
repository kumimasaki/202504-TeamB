package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.com.model.entity.Comment;
import ec.com.model.entity.TransactionItem;
import jakarta.transaction.Transactional;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long>{
	Comment save(Comment like);
	List<Comment> findAll();
	List<Comment> findByLessonId(Long lessonId);
}