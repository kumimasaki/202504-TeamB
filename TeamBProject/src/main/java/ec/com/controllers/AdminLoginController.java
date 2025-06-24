package ec.com.controllers;

import ec.com.model.entity.Admin;
import ec.com.services.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 管理者用ログインコントローラ
 * <ul>
 * <li>ログイン画面表示</li>
 * <li>認証処理</li>
 * <li>ログアウト処理</li>
 * </ul>
 */
@Controller
public class AdminLoginController {

	@Autowired
	// 認証・セッション管理を行うサービス
	private AdminService adminService;

	@Autowired
	// HTTPセッション管理用オブジェクト
	private HttpSession session;

	/**
	 * 管理者ログイン画面を表示する
	 * <ol>
	 * <li>リクエストパラメータerrorがある場合、エラーメッセージを設定</li>
	 * <li>admin_loginテンプレートを返却</li>
	 * </ol>
	 *
	 * @param error リダイレクト時に付加されるエラークエリパラメータ（任意）
	 * @param model ビューに渡すモデル（エラーメッセージ格納用）
	 * @return ログイン画面テンプレート名
	 */
	@GetMapping("/admin/login")
	public String showLoginForm(@RequestParam(name = "error", required = false) String error, Model model) {
		// errorパラメータが存在する場合はログイン失敗メッセージを表示
		if (error != null) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが間違っています。");
		}
		// ログイン画面にフォワード
		return "admin_login";
	}

	/**
	 * ログイン処理を行う
	 * <ol>
	 * <li>AdminServiceで認証チェックを実行</li>
	 * <li>認証失敗: ログイン画面へリダイレクト（errorパラメータ付与）</li>
	 * <li>認証成功: セッションにAdminオブジェクトを保存、サービスに成功通知、講座一覧へリダイレクト</li>
	 * </ol>
	 *
	 * @param email    入力されたメールアドレス
	 * @param password 入力されたパスワード
	 * @return 認証結果に応じたリダイレクトURL
	 */
	@PostMapping("/admin/login")
	public String loginProcess(@RequestParam String email, @RequestParam String password) {
		// 認証チェック: 正しい場合はAdminオブジェクトを返却、失敗時はnull
		Admin admin = adminService.loginCheck(email, password);

		if (admin == null) {
			// 認証失敗: ログイン画面へリダイレクトし、errorパラメータを付与
			return "redirect:/admin/login?error=true";
		} else {
			// 認証成功: セッションにユーザー情報を格納
			session.setAttribute("AdminLogin", admin);
			// サービスにログイン成功を通知（ログ記録や追加処理）
			adminService.loginSuccess(session, admin);
			// 講座一覧画面へリダイレクト
			return "redirect:/admin/lesson/all";
		}
	}

	/**
	 * ログアウト処理を行う
	 * <ol>
	 * <li>AdminServiceでセッション破棄などの後処理を実行</li>
	 * <li>ログイン画面へリダイレクト</li>
	 * </ol>
	 *
	 * @return ログイン画面へのリダイレクトURL
	 */
	@GetMapping("/admin/logout")
	public String logout() {
		// サービスにログアウト処理を委譲（セッション破棄等）
		adminService.logout(session);
		return "redirect:/admin/login";
	}

}
