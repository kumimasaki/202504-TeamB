package ec.com.model.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat; 

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lessonId;

	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private LocalDate startDate;

	@DateTimeFormat(pattern = "HH:mm") 
	private LocalTime startTime;

	@DateTimeFormat(pattern = "HH:mm") 
	private LocalTime finishTime;

	private String lessonName;
	private String lessonDetail;
	private Integer lessonFee;
	private String imageName;
	private Timestamp registerDate;
	// 講座の定員（人数上限）
	private Integer capacity;

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
	

	// Getter & Setter
	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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
	

	// --- Constructors ---
	public Lesson() {}
	public Lesson(LocalDate startDate, LocalTime startTime, LocalTime finishTime,
            String lessonName, String lessonDetail, Integer lessonFee, String imageName,
            Timestamp registerDate, Integer capacity) {
  this.startDate = startDate;
  this.startTime = startTime;
  this.finishTime = finishTime;
  this.lessonName = lessonName;
  this.lessonDetail = lessonDetail;
  this.lessonFee = lessonFee;
  this.imageName = imageName;
  this.registerDate = registerDate;
  this.capacity = capacity;
}

}
