package ec.com.services;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.model.dao.LessonDao;
import ec.com.model.dao.TransactionHistoryDao;
import ec.com.model.dto.LessonWithTransactionDto;
import ec.com.model.entity.Lesson;

@Service
public class LessonService {

	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private TransactionHistoryDao transactionHistoryDao;
	/**
	 * すべての講座を取得する
	 * 
	 * @return 講座リスト
	 */
	public List<Lesson> findAll() {
		return lessonDao.findAll();
	}

	/**
	 * 新しい講座を登録する
	 * 
	 * @param lesson 登録する講座
	 */
	public void registerLesson(Lesson lesson) {
		lessonDao.save(lesson);
	}

	/**
	 * IDに対応する講座を1件取得
	 * 
	 * @param lessonId 講座ID
	 * @return 該当する講座（存在しない場合はnull）
	 */
	public Lesson findById(Long lessonId) {
		return lessonDao.findById(lessonId).orElse(null);
	}

	/**
	 * 管理者IDに紐づく講座をすべて取得
	 * 
	 * @param adminId 管理者ID
	 * @return 管理者の講座一覧
	 */
	public List<Lesson> selectAllLessonList(Long adminId) {
		return lessonDao.findByAdminId(adminId);
	}

	/**
	 * 講座情報を更新（画像情報も含む）
	 * 
	 * @param lesson 更新内容が入った講座エンティティ
	 */
	public void updateLesson(Lesson lesson) {
		lessonDao.save(lesson);
	}


	public void insertLesson(Lesson lesson) {
		lessonDao.save(lesson);
	}

	/**
	 * 講座IDに基づいて講座を削除する
	 * 
	 * @param lessonId 削除対象の講座ID
	 * @return 削除成功ならtrue、失敗（null）の場合はfalse
	 */
	public boolean deletByLesson(Long lessonId) {
		if (lessonId == null) {
			return false;
		} else {
			lessonDao.deleteById(lessonId);
			return true;
		}

	}

  	/**
	 * 指定されたユーザーIDに紐づく購入講座（Lesson）を取得する
	 *
	 * @param userId ユーザーのID
	 * @return 購入済みの講座のリスト
	 */
	public List<LessonWithTransactionDto> getLessonPurchases(Long userId) {
		 List<Object[]> result = transactionHistoryDao.findLessonAndTransactionByUserId(userId);
		    List<LessonWithTransactionDto> list = new ArrayList<>();
		    for (Object[] row : result) {
		        LessonWithTransactionDto dto = new LessonWithTransactionDto();
		        dto.setLessonId(((Number) row[0]).longValue());
		        dto.setLessonName((String) row[1]);
		        dto.setLessonDetail((String) row[2]);
		        dto.setImageName((String) row[3]);
		        dto.setStartDate(((Date) row[4]).toLocalDate());
		        dto.setStartTime(((Time) row[5]).toLocalTime());
		        dto.setFinishTime(((Time) row[6]).toLocalTime());
		        dto.setLessonFee((Integer) row[7]);
		        dto.setTransactionDate(((Timestamp) row[8]).toLocalDateTime().toLocalDate());
		        dto.setTransactionId(((Number) row[9]).longValue());
		        list.add(dto);
		    }
		return list;
	}
}
