package ec.com.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.model.dao.LessonDao;
import ec.com.model.entity.Lesson;

@Service
public class LessonService {

	@Autowired
	private LessonDao lessonDao;

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
}
