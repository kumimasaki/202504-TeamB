package ec.com.services;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.model.dao.LessonDao;
import ec.com.model.dao.LikeDao;
import ec.com.model.dao.TransactionHistoryDao;
import ec.com.model.dao.TransactionItemDao;
import ec.com.model.dto.LessonWithTransactionDto;
import ec.com.model.entity.Lesson;
import ec.com.model.entity.TransactionItem;
import jakarta.transaction.Transactional;
import ec.com.model.dto.LessonStatsWithInfoDto;

@Service
public class LessonService {

    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private TransactionHistoryDao transactionHistoryDao;
    @Autowired
    private TransactionItemDao transactionItemDao;
    @Autowired
    private LikeDao likeDao;

    // 講座全件取得
    public List<Lesson> findAll() {
        return lessonDao.findAll();
    }

    // 新規登録
    public void registerLesson(Lesson lesson) {
        lessonDao.save(lesson);
    }

    // ID指定で1件取得
    public Lesson findById(Long lessonId) {
        return lessonDao.findById(lessonId).orElse(null);
    }

    // 講座更新（画像含む）
    public void updateLesson(Lesson lesson) {
        lessonDao.save(lesson);
    }

    // 講座削除
    public boolean deletByLesson(Long lessonId) {
        if (lessonId == null) {
            return false;
        } else {
            lessonDao.deleteById(lessonId);
            return true;
        }
    }

    // キーワード検索（講座番号・講座名）
    public List<Lesson> searchLessonByKeyword(String keyword) {
        return lessonDao.searchByKeyword(keyword);
    }

    // 購入済み講座取得（ユーザー別）
    public List<LessonWithTransactionDto> getLessonPurchases(Long userId) {
        List<Object[]> result = transactionHistoryDao.findLessonAndTransactionByUserId(userId);
        return convertToDto(result);
    }

    public List<LessonWithTransactionDto> getLessonPurchases(Long userId, Integer buyTime) {
        List<Object[]> resultList;
        LocalDateTime now = LocalDateTime.now();

        if (buyTime == 1) {
            resultList = transactionHistoryDao.findByUserIdAndFromDate(userId, Timestamp.valueOf(now.toLocalDate().atStartOfDay()));
        } else if (buyTime == 2) {
            resultList = transactionHistoryDao.findByUserIdAndFromDate(userId, Timestamp.valueOf(now.minusMonths(1)));
        } else if (buyTime == 3) {
            resultList = transactionHistoryDao.findByUserIdAndFromDate(userId, Timestamp.valueOf(now.minusYears(1)));
        } else {
            resultList = transactionHistoryDao.findLessonAndTransactionByUserId(userId);
        }

        return convertToDto(resultList);
    }

    private List<LessonWithTransactionDto> convertToDto(List<Object[]> result) {
        List<LessonWithTransactionDto> list = new ArrayList<>();
        for (Object[] row : result) {
            LessonWithTransactionDto dto = new LessonWithTransactionDto();
            dto.setLessonId(((Long) row[0]).longValue());
            dto.setLessonName((String) row[1]);
            dto.setLessonDetail((String) row[2]);
            dto.setImageName((String) row[3]);
            dto.setStartDate(((Date) row[4]).toLocalDate());
            dto.setStartTime(((Time) row[5]).toLocalTime());
            dto.setFinishTime(((Time) row[6]).toLocalTime());
            dto.setLessonFee((Integer) row[7]);
            dto.setTransactionDate(((Timestamp) row[8]).toLocalDateTime().toLocalDate());
            dto.setTransactionId(((Number) row[9]).longValue());
            dto.setId(((Number) row[10]).longValue());
            list.add(dto);
        }
        return list;
    }

    // お気に入り数ランキング
    public List<Map<String, Object>> getLikeRanking() {
        List<Object[]> ranking = likeDao.getLessonLikeRanking();
        List<Map<String, Object>> rankingList = new ArrayList<>();

        for (Object[] row : ranking) {
            Long lessonId = (Long) row[0];
            Long likeCount = (Long) row[1];
            Lesson lesson = lessonDao.findByLessonId(lessonId);

            Map<String, Object> map = new HashMap<>();
            map.put("lesson", lesson);
            map.put("likeCount", likeCount);
            rankingList.add(map);
        }
        return rankingList;
    }

    // 購入統計（講座ごとの売上など）
    public List<LessonStatsWithInfoDto> getLessonStatsList() {
        List<Object[]> results = transactionHistoryDao.countApplicationsAndSalesPerLesson();
        List<Lesson> lessons = lessonDao.findAll();
        List<LessonStatsWithInfoDto> statsList = new ArrayList<>();

        for (Lesson lesson : lessons) {
            LessonStatsWithInfoDto dto = new LessonStatsWithInfoDto();
            dto.setLessonId(lesson.getLessonId());
            dto.setLessonName(lesson.getLessonName());
            dto.setLessonFee(lesson.getLessonFee());
            dto.setCapacity(lesson.getCapacity());
            dto.setApplyCount(0);
            dto.setTotalSales(0);

            for (Object[] row : results) {
                if (row[0] != null && lesson.getLessonId().equals(Long.parseLong(row[0].toString()))) {
                    int applyCount = row[3] != null ? Integer.parseInt(row[3].toString()) : 0;
                    dto.setApplyCount(applyCount);
                    dto.setTotalSales(lesson.getLessonFee() * applyCount);
                    break;
                }
            }

            statsList.add(dto);
        }

        return statsList;
    }

    // トランザクション履歴削除
    @Transactional
    public void deleteTransactionItemAndHistory(Long id) {
        TransactionItem item = transactionItemDao.findById(id).orElse(null);
        if (item == null) return;

        Long transactionId = item.getTransactionId();
        transactionItemDao.deleteById(id);

        boolean hasItems = transactionItemDao.existsByTransactionId(transactionId);
        if (!hasItems) {
            transactionHistoryDao.deleteById(transactionId);
        }
    }

	// lesson削除前に取引記録の存在チェックを追加
	public boolean hasTransaction(Long lessonId) {
		return transactionItemDao.existsByLessonId(lessonId);
	}

}

