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
     * 講座名による部分一致検索（管理者IDも指定）
     *
     * @param adminId 管理者ID
     * @param keyword 講座名キーワード
     * @return 検索結果の講座リスト
     */
    public List<Lesson> searchLessonByKeyword(Long adminId, String keyword) {
        return lessonDao.searchByKeyword(adminId, keyword);
    }

  /** 指定されたユーザーIDに紐づく購入済み講座情報を取得する。
	 * 講座情報と購入日時などを含むDTO（LessonWithTransactionDto）のリストを返す。
	 *
	 * @param userId ユーザーのID
	 * @return 購入済み講座のリスト
	 */
	public List<LessonWithTransactionDto> getLessonPurchases(Long userId) {
		 List<Object[]> result = transactionHistoryDao.findLessonAndTransactionByUserId(userId);
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
	
	/**
	 *  購入履歴の削除を行う。
	 *  選択されたitemをDBから削除処理
	 *  その後、transactionHistoryを削除する。
	 *  transactionHistoryはitemに紐づいているtransaction_idがすべて削除されたら
	 *  transactionHistoryからも削除する
	 * @param transactionId
	 * 
	 */
	@Transactional
		public void deleteTransactionItemAndHistory(Long id) {
		
		TransactionItem item = transactionItemDao.findById(id).orElse(null);
		// IDに該当するデータが存在しない場合は何もしない
		if (item == null) {
	        return; 
	    }

		Long transactionId = item.getTransactionId();
		
		// 選択itemを削除
		transactionItemDao.deleteById(id);
		
		
		boolean hasItems =transactionItemDao.existsByTransactionId(transactionId);
		
		 if (!hasItems) {
		        transactionHistoryDao.deleteById(transactionId);
		    }
	}
	
	public List<LessonStatsWithInfoDto> getLessonStatsList(Long adminId) {
	    // 統計データ（講座ID, 申込数, 売上）
	    List<Object[]> results = transactionHistoryDao.countApplicationsAndSalesPerLesson();
	    // 講座情報（講座ID, 名前, 料金...）
	    List<Lesson> lessons = lessonDao.findByAdminId(adminId);

	    List<LessonStatsWithInfoDto> statsList = new ArrayList<>();

	    for (Lesson lesson : lessons) {
	        LessonStatsWithInfoDto dto = new LessonStatsWithInfoDto();
	        dto.setLessonId(lesson.getLessonId());
	        dto.setLessonName(lesson.getLessonName());
	        dto.setLessonFee(lesson.getLessonFee());

	        // 初期値（該当する統計がない場合）
	        dto.setApplyCount(0);
	        dto.setTotalSales(0);

	     // 統計結果と照合
	        for (Object[] row : results) {
	            // row[0] 是 lessonId
	            if (row[0] != null && lesson.getLessonId().equals(Long.parseLong(row[0].toString()))) {
	                // row[3] 是申込人数（apply_count），先取出来
	                int applyCount = row[3] != null ? Integer.parseInt(row[3].toString()) : 0;

	                dto.setApplyCount(applyCount);
	                dto.setTotalSales(lesson.getLessonFee() * applyCount); // 直接用 fee * 人数
	                break;
	            }
	        }
	       

	        statsList.add(dto);
	    }

	    return statsList;
	}
}
