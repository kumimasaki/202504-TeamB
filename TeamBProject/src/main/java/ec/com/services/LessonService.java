package ec.com.services;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.model.dao.LessonDao;
import ec.com.model.dao.LikeDao;
import ec.com.model.dao.TransactionHistoryDao;
import ec.com.model.dao.TransactionItemDao;
import ec.com.model.dto.LessonWithStatDto;
import ec.com.model.dto.LessonWithTransactionDto;
import ec.com.model.dto.LessonStatsWithInfoDto;
import ec.com.model.entity.Lesson;

/**
 * 講座関連のビジネスロジックを提供するサービスクラス
 * <ul>
 * <li>講座一覧取得</li>
 * <li>講座登録・更新・削除</li>
 * <li>キーワード検索</li>
 * <li>購入履歴や統計情報の取得</li>
 * <li>お気に入りランキング</li>
 * <li>トランザクション削除（非表示化）</li>
 * <li>DTO変換処理</li>
 * </ul>
 */
@Service
public class LessonService {

	@Autowired
	// 講座データアクセスオブジェクト
	private LessonDao lessonDao;
	@Autowired
	// 購入履歴DAO
	private TransactionHistoryDao transactionHistoryDao;
	@Autowired
	// 購入明細DAO
	private TransactionItemDao transactionItemDao;
	@Autowired
	// お気に入りDAO
	private LikeDao likeDao;

	/**
	 * すべての講座を取得する
	 * 
	 * @return 講座エンティティのリスト
	 */
	public List<Lesson> findAll() {
		return lessonDao.findAll();
	}

	/**
	 * 新規講座を登録する
	 * 
	 * @param lesson 登録する講座エンティティ
	 */
	public void registerLesson(Lesson lesson) {
		lessonDao.save(lesson);
	}

	/**
	 * lessonIdを指定して講座を取得する
	 * 
	 * @param lessonId 講座ID
	 * @return 該当講座、存在しない場合はnull
	 */
	public Lesson findById(Long lessonId) {
		return lessonDao.findById(lessonId).orElse(null);
	}

	/**
	 * 講座情報を更新する
	 * 
	 * @param lesson 更新対象の講座エンティティ
	 */
	public void updateLesson(Lesson lesson) {
		lessonDao.save(lesson);
	}

	/**
	 * 講座を削除する
	 * 
	 * @param lessonId 削除対象の講座ID
	 * @return 削除成功ならtrue、IDがnullならfalse
	 */
	public boolean deletByLesson(Long lessonId) {
		if (lessonId == null) {
			return false;
		}
		lessonDao.deleteById(lessonId);
		return true;
	}

	/**
	 * キーワード検索で講座を検索する
	 * 
	 * @param keyword 検索キーワード（番号 or 名称）
	 * @return 検索結果の講座リスト
	 */
	public List<Lesson> searchLessonByKeyword(String keyword) {
		return lessonDao.searchByKeyword(keyword);
	}

	/**
	 * 指定ユーザーの購入履歴を取得する
	 * 
	 * @param userId ユーザーID
	 * @return 購入履歴DTOのリスト
	 */
	public List<LessonWithTransactionDto> getLessonPurchases(Long userId) {
		List<Object[]> result = transactionHistoryDao.findLessonAndTransactionByUserId(userId);
		return convertToDto(result);
	}

	/**
	 * 指定期間の購入履歴を取得する（1:当日,2:1ヶ月以内,3:1年以内,その他:全期間）
	 * 
	 * @param userId  ユーザーID
	 * @param buyTime 期間コード
	 * @return 購入履歴DTOのリスト
	 */
	public List<LessonWithTransactionDto> getLessonPurchases(Long userId, Integer buyTime) {
		List<Object[]> resultList;
		LocalDateTime now = LocalDateTime.now();
		// 期間別にDAOメソッドを呼び分け
		if (buyTime == 1) {
			resultList = transactionHistoryDao.findByUserIdAndFromDate(userId,
					Timestamp.valueOf(now.toLocalDate().atStartOfDay()));
		} else if (buyTime == 2) {
			resultList = transactionHistoryDao.findByUserIdAndFromDate(userId, Timestamp.valueOf(now.minusMonths(1)));
		} else if (buyTime == 3) {
			resultList = transactionHistoryDao.findByUserIdAndFromDate(userId, Timestamp.valueOf(now.minusYears(1)));
		} else {
			resultList = transactionHistoryDao.findLessonAndTransactionByUserId(userId);
		}
		return convertToDto(resultList);
	}

	/**
	 * DAOから取得したObject[]配列をDTOに変換する
	 * 
	 * @param result DAOが返すObject[]のリスト
	 * @return DTOのリスト
	 */
	private List<LessonWithTransactionDto> convertToDto(List<Object[]> result) {
		List<LessonWithTransactionDto> list = new ArrayList<>();
		for (Object[] row : result) {
			LessonWithTransactionDto dto = new LessonWithTransactionDto();
			dto.setLessonId(((Long) row[0]));
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
	 * 講座のお気に入り数ランキングを取得する
	 * 
	 * @return lessonエンティティとlikeCountを格納したMapのリスト
	 */
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

	/**
	 * 各講座の申込件数と売上を集計し、DTOにまとめて返す
	 * 
	 * @return 統計情報DTOのリスト
	 */
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
			// 結果セットから該当講座を探し出して値を設定
			for (Object[] row : results) {
				if (row[0] != null && lesson.getLessonId().equals(Long.valueOf(row[0].toString()))) {
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

	/**
	 * トランザクション明細を非表示化（論理削除）
	 * 
	 * @param id トランザクションアイテムID
	 */
	@Transactional
	public void deleteTransactionItemAndHistory(Long id) {
		transactionItemDao.updateIsVisibleById(id);
	}

	/**
	 * 講座削除前に購入履歴の存在を確認する
	 * 
	 * @param lessonId 講座ID
	 * @return 購入履歴があればtrue、なければfalse
	 */
	public boolean hasTransaction(Long lessonId) {
		return transactionItemDao.existsByLessonId(lessonId);
	}

	/**
	 * 今後開催予定の講座を取得し、統計情報付きDTOに変換する
	 * 
	 * @param fromTime 取得開始日時
	 * @return 予定講座DTOのリスト
	 */
	public List<LessonWithStatDto> getLessonDto(LocalDateTime fromTime) {
		List<Lesson> lessons = lessonDao.findUpcomingLessons(fromTime);
		List<LessonWithStatDto> dtoList = new ArrayList<>();
		for (Lesson lesson : lessons) {
			int applyCount = (int) transactionItemDao.countByLessonId(lesson.getLessonId());
			int capacity = lesson.getCapacity();
			LessonWithStatDto dto = new LessonWithStatDto();
			dto.setLessonId(lesson.getLessonId());
			dto.setLessonName(lesson.getLessonName());
			dto.setLessonDetail(lesson.getLessonDetail());
			dto.setImageName(lesson.getImageName());
			dto.setStartDate(lesson.getStartDate());
			dto.setStartTime(lesson.getStartTime());
			dto.setFinishTime(lesson.getFinishTime());
			dto.setLessonFee(lesson.getLessonFee());
			dto.setApplyCount(applyCount);
			dto.setCapacity(capacity);
			dtoList.add(dto);
		}
		return dtoList;
	}
}
