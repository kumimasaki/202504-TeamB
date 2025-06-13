package ec.com.model.entity;

import java.sql.Timestamp;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Likes")
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	private Long lessonId;
	private Long userId;
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Like() {
	}
	
	public Like(Long lessonId, Long userId) {
		this.lessonId = lessonId;
		this.userId = userId;
	}
}
