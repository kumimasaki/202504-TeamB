package ec.com.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.repository.query.Param;

public class LessonWithTransactionDto {
	private Long lessonId;
	private String lessonName;
	private String lessonDetail;
	private String imageName;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalTime finishTime;
	private Integer lessonFee;
	private LocalDate transactionDate;
	private Long transactionId;
	private Long id;

	public Long getLessonId() {
		return lessonId;
	}

	// getter,setter
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
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

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
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

	public Integer getLessonFee() {
		return lessonFee;
	}

	public void setLessonFee(Integer lessonFee) {
		this.lessonFee = lessonFee;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
