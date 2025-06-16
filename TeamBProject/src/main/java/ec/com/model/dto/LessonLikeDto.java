package ec.com.model.dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import ec.com.model.entity.Lesson;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class LessonLikeDto {
	private Long lessonId;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalTime finishTime;
	private String lessonName;
	private String lessonDetail;
	private Integer lessonFee;
	private String imageName;
	private Timestamp registerDate;
	private Long adminId;
	private boolean liked;

	// --- Getter & Setter ---
	public Long getLessonId() {
		return lessonId;
	}
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public String getLessonDetail() {
		return lessonDetail;
	}
	public void setLessonDetail(String lessonDetail) {
		this.lessonDetail = lessonDetail;
	}
	public Integer getLessonFee() {
		return lessonFee;
	}
	public void setLessonFee(Integer lessonFee) {
		this.lessonFee = lessonFee;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public Timestamp getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public boolean isLiked() {
		return liked;
	}
	public void setLiked(boolean liked) {
		this.liked = liked;
	}
	
	// --- Constructors ---
	public LessonLikeDto() {}

	public LessonLikeDto(Lesson lesson) {
		this.lessonId = lesson.getLessonId();
		this.startDate = lesson.getStartDate();
		this.startTime = lesson.getStartTime();
		this.finishTime = lesson.getFinishTime();
		this.lessonName = lesson.getImageName();
		this.lessonDetail = lesson.getLessonDetail();
		this.lessonFee = lesson.getLessonFee();
		this.imageName = lesson.getImageName();
		this.registerDate = lesson.getRegisterDate();
		this.adminId = lesson.getAdminId();
		this.liked = true;
	}
}
