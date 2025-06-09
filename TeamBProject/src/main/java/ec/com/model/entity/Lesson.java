package ec.com.model.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	public Lesson() {
	}
	
	public Lesson(Long lessonId, LocalDate startDate, LocalTime startTime, LocalTime finishTime, String lessonName,
			String lessonDetail, Integer lessonFee, String imageName, Timestamp registerDate, Long adminId) {
		this.lessonId = lessonId;
		this.startDate = startDate;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.lessonName = lessonName;
		this.lessonDetail = lessonDetail;
		this.lessonFee = lessonFee;
		this.imageName = imageName;
		this.registerDate = registerDate;
		this.adminId = adminId;
	}
}
