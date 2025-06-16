package ec.com.model.dto;

/**
 * 管理者用：講座の基本情報 + 申込人数 + 売上金額 をまとめたDTO
 */
public class LessonStatsWithInfoDto {
    private Long lessonId;
    private String lessonName;
    private Integer lessonFee;
    private Integer applyCount;
    private Integer totalSales;

    // --- Getter & Setter ---
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

    public Integer getLessonFee() {
        return lessonFee;
    }

    public void setLessonFee(Integer lessonFee) {
        this.lessonFee = lessonFee;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
}

