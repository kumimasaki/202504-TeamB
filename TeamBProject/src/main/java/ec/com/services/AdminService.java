package ec.com.services;

import ec.com.model.dao.AdminDao;
import ec.com.model.entity.Admin;
import jakarta.servlet.http.HttpSession;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理者認証・登録関連のビジネスロジックを提供するサービスクラス
 * <ul>
 *   <li>ログインチェック</li>
 *   <li>ログイン成功時のセッション設定</li>
 *   <li>ログアウト処理</li>
 *   <li>管理者の新規登録処理</li>
 * </ul>
 */
@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;  // 管理者情報のCRUDを行うDAO

    /**
     * ログイン認証を行う
     * <ol>
     *   <li>DAOを使用し、メールアドレスとパスワードで該当Adminを検索</li>
     *   <li>一致するAdminが存在すれば返却、なければnullを返却</li>
     * </ol>
     *
     * @param email    入力されたメールアドレス
     * @param password 入力されたパスワード
     * @return 認証済みAdminオブジェクト、認証失敗時はnull
     */
    public Admin loginCheck(String email, String password) {
        return adminDao.findByAdminEmailAndAdminPassword(email, password);
    }

    /**
     * ログイン成功時の後処理
     * <ul>
     *   <li>セッションにAdminオブジェクトを"admin"属性で保存</li>
     * </ul>
     *
     * @param session HTTPセッション
     * @param admin   認証済みAdminオブジェクト
     */
    public void loginSuccess(HttpSession session, Admin admin) {
        session.setAttribute("admin", admin);
    }

    /**
     * ログアウト処理
     * <ul>
     *   <li>セッションを無効化して全属性をクリア</li>
     * </ul>
     *
     * @param session HTTPセッション
     */
    public void logout(HttpSession session) {
        session.invalidate();
    }

    /**
     * 管理者の新規登録処理
     * <ol>
     *   <li>指定のメールアドレスが未登録か確認</li>
     *   <li>未登録の場合、Adminエンティティを生成し項目をセット</li>
     *   <li>DAOを使用してDBに保存</li>
     *   <li>登録成功時はtrue、重複時はfalseを返却</li>
     * </ol>
     *
     * @param adminName     管理者名
     * @param adminEmail    メールアドレス
     * @param adminPassword パスワード
     * @return 登録成功時はtrue、既に登録済みの場合はfalse
     */
    public boolean createAdmin(String adminName, String adminEmail, String adminPassword) {
        // メールアドレスの重複チェック
        if (adminDao.findByAdminEmail(adminEmail) == null) {
            // 新規Adminエンティティを作成
            Admin newAdmin = new Admin();
            newAdmin.setAdminName(adminName);
            newAdmin.setAdminEmail(adminEmail);
            newAdmin.setAdminPassword(adminPassword);
            newAdmin.setDeleteFlg(0);  // 論理削除フラグ初期値
            newAdmin.setRegisterDate(new Timestamp(System.currentTimeMillis()));  // 登録日時セット
            // DBに保存
            adminDao.save(newAdmin);
            return true;
        } else {
            // 重複がある場合は登録しない
            return false;
        }
    }
}

