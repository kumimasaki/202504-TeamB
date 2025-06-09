package ec.com.services;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ec.com.model.dao.UserDao;
import ec.com.model.entity.User;

/**
 * ユーザーサービスクラス ユーザーに関するビジネスロジックを処理する
 */
@Service
public class UserService {

	/**
	 * ユーザーDAOの依存性注入 データベースアクセスを行うためのDAO
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * ユーザー登録処理メソッド 新規ユーザーをデータベースに登録する
	 * 
	 * @param userEmail    ユーザーのメールアドレス
	 * @param userName     ユーザーの氏名
	 * @param userPassword ユーザーのパスワード
	 * @return boolean 登録成功の場合true、失敗の場合false
	 */
	public boolean createUser(String userEmail, String userName, String userPassword) {
		try {
			// 既存のメールアドレスチェック
			// 同じメールアドレスが既に登録されているかを確認
			if (userDao.findByUserEmail(userEmail) != null) {
				// 既に登録済みのメールアドレスの場合、falseを返す
				return false;
			}

			// 新しいユーザーオブジェクトを作成
			User user = new User();

			// ユーザー情報を設定
			user.setUserName(userName); // ユーザー名を設定
			user.setUserEmail(userEmail); // メールアドレスを設定
			user.setUserPassword(userPassword); // パスワードを設定
			user.setDeleteFlg(0); // 削除フラグを0（アクティブ）に設定
			user.setRegisterDate(new Timestamp(System.currentTimeMillis())); // 現在時刻を登録日時に設定

			// データベースに保存
			userDao.save(user);

			// 登録成功
			return true;

		} catch (Exception e) {
			// エラーが発生した場合、コンソールにエラーメッセージを出力
			System.out.println("ユーザー登録エラー: " + e.getMessage());
			// 登録失敗
			return false;
		}
	}

	/**
	 * ユーザーログイン認証メソッド メールアドレスとパスワードでユーザー認証を行う
	 * 
	 * @param userEmail    メールアドレス
	 * @param userPassword パスワード
	 * @return User 認証成功の場合ユーザーオブジェクト、失敗の場合null
	 */
	public User loginUser(String userEmail, String userPassword) {
		return userDao.findByUserEmailAndUserPassword(userEmail, userPassword);
	}

	/**
	 * 全ユーザー取得メソッド データベースに登録されている全ユーザーを取得する
	 * 
	 * @return List<User> ユーザーリスト
	 */
	public List<User> findAllUsers() {
		return userDao.findAll();
	}

	/**
	 * ユーザーID検索メソッド 指定されたユーザーIDでユーザーを検索する
	 * 
	 * @param userId ユーザーID
	 * @return User ユーザーオブジェクト
	 */
	public User findByUserId(Long userId) {
		return userDao.findByUserId(userId);
	}

	/**
	 * メールアドレス検索メソッド 指定されたメールアドレスでユーザーを検索する
	 * 
	 * @param userEmail メールアドレス
	 * @return User ユーザーオブジェクト
	 */
	public User findByUserEmail(String userEmail) {
		return userDao.findByUserEmail(userEmail);
	}

	/**
	 * ユーザー更新メソッド 既存ユーザーの情報を更新する
	 * 
	 * @param user 更新するユーザーオブジェクト
	 * @return User 更新されたユーザーオブジェクト
	 */
	public User updateUser(User user) {
		return userDao.save(user);
	}

	/**
	 * ユーザー削除メソッド（論理削除） ユーザーを論理削除する（delete_flgを1に設定）
	 * 
	 * @param userId 削除するユーザーのID
	 * @return boolean 削除成功の場合true、失敗の場合false
	 */
	public boolean deleteUser(Long userId) {
		try {
			User user = userDao.findByUserId(userId);
			if (user != null) {
				user.setDeleteFlg(1); // 論理削除フラグを設定
				userDao.save(user);
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("ユーザー削除エラー: " + e.getMessage());
			return false;
		}
	}
}