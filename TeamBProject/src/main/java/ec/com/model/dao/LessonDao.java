package ec.com.model.dao;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import ec.com.model.entity.Lesson;

/**
 * Lessonエンティティに対するデータアクセスを提供するDAOインターフェース
 * JpaRepositoryを継承し、標準的なCRUD操作に加え、カスタムクエリを定義します。
 */
@Repository
@Transactional
public interface LessonDao extends JpaRepository<Lesson, Long> {

    /**
     * 新規登録および更新用のsaveメソッド
     * @param lesson 保存または更新するLessonエンティティ
     * @return 永続化されたLessonエンティティ
     */
    @Override
    Lesson save(Lesson lesson);

    /**
     * すべての講座を取得します。
     * @return 講座エンティティのリスト
     */
    @Override
    List<Lesson> findAll();

    /**
     * 講座IDを指定して1件の講座を取得します。
     * @param lessonId 検索対象の講座ID
     * @return 該当するLessonエンティティ、存在しない場合null
     */
    Lesson findByLessonId(Long lessonId);

    /**
     * 現在時刻以降に開始する講座のみを取得します。
     * <ul>
     *   <li>日付と時刻を連結してtimestampにキャスト</li>
     *   <li>現在日時(currentDateTime)以上の講座を抽出</li>
     *   <li>開始日と開始時刻でソート</li>
     * </ul>
     * @param currentDateTime 比較対象の現在日時
     * @return 今後開催予定の講座リスト
     */
    @Query("SELECT l FROM Lesson l " +
           "WHERE CAST(CONCAT(l.startDate, ' ', l.startTime) AS timestamp) >= :currentDateTime " +
           "ORDER BY l.startDate, l.startTime")
    List<Lesson> findUpcomingLessons(@Param("currentDateTime") LocalDateTime currentDateTime);

    /**
     * キーワードで講座名または詳細を部分一致検索し、
     * 指定日時以降に開始する講座を取得します。
     * @param keyword 検索キーワード
     * @param date 開始日比較用
     * @param time 開始時間比較用
     * @return 条件を満たす講座リスト
     */
    @Query("""
            SELECT l FROM Lesson l
            WHERE (l.lessonName LIKE %:keyword% OR l.lessonDetail LIKE %:keyword%)
              AND (l.startDate > :date OR (l.startDate = :date AND l.startTime >= :time))
        """ )
    List<Lesson> findByLessonNameContainingAndDateTimeCondition(
        @Param("keyword") String keyword,
        @Param("date") LocalDate date,
        @Param("time") LocalTime time
    );

    /**
     * 講座をID指定で削除します。
     * @param lessonId 削除対象の講座ID
     */
    @Override
    void deleteById(Long lessonId);

    /**
     * 講座番号または講座名にキーワードが含まれる講座を検索します。
     * @param keyword LIKE句用のキーワード（"%keyword%"でワイルドカード検索）
     * @return 部分一致した講座リスト
     */
    @Query("SELECT l FROM Lesson l WHERE CAST(l.lessonId AS string) LIKE :keyword OR l.lessonName LIKE :keyword")
    List<Lesson> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 講座に紐づく取引明細の件数をネイティブSQLで取得します。
     * @param lessonId 対象の講座ID
     * @return 取引明細テーブルにある当該講座の出現回数
     */
    @Query(value = "SELECT COUNT(*) FROM transaction_item t2 " +
                   "LEFT JOIN lesson t3 ON t2.lesson_id = t3.lesson_id " +
                   "WHERE t3.lesson_id = :lessonId", nativeQuery = true)
    Integer findLessonCountByLessonId(@Param("lessonId") Long lessonId);
}
