package ec.com.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

	//lessonに定員を表示させるためのDto
public class LessonWithStatDto {
	    private Long lessonId;
	    private String lessonName;
	    private String lessonDetail;
	    private String imageName;
	    private LocalDate startDate;
	    private LocalTime startTime;
	    private LocalTime finishTime;
	    private Integer lessonFee;
	    private int applyCount;
	    private int capacity;

	    public String getCapacityText() {
	        return applyCount + "/" + capacity;
	    }

	    public boolean isFull() {
	        return applyCount >= capacity;
	    }

		public Long getLessonId() {
			return lessonId;
		}

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

		public int getApplyCount() {
			return applyCount;
		}

		public void setApplyCount(int applyCount) {
			this.applyCount = applyCount;
		}

		public int getCapacity() {
			return capacity;
		}

		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
	    
}


