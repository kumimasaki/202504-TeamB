package ec.com.controllers;

import ec.com.model.entity.Admin;
import ec.com.services.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private HttpSession session;

	/**
	 * 管理者ログイン画面を表示する
	 * @param error ログインエラーがある場合にエラーメッセージを表示
	 * @param model エラーメッセージを画面に渡すためのモデル
	 * @return admin_login.html というログイン画面のテンプレート
	 */
	@GetMapping("/admin/login")
	public String showLoginForm(@RequestParam(name = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが間違っています。");
		}
		return "admin_login";  
	}

	/**
	 * ログイン処理を行う
	 * @param email 入力されたメールアドレス
	 * @param password 入力されたパスワード
	 * @return ログイン成功時は講座一覧画面へリダイレクト、失敗時はログイン画面へ戻る
	 */
	@PostMapping("/admin/login")
	public String loginProcess(@RequestParam String email, @RequestParam String password) {
		Admin admin = adminService.loginCheck(email, password);

		if (admin == null) {
			return "redirect:/admin/login?error=true";
		} else {
			session.setAttribute("AdminLogin", admin);
			adminService.loginSuccess(session, admin);
			return "redirect:/admin/lesson/all";
		}
	}

	/**
	 * ログアウト処理を行う
	 * セッションを破棄し、ログイン画面へ戻る
	 * @return ログイン画面へリダイレクト
	 */
	@GetMapping("/admin/logout")
	public String logout() {
		adminService.logout(session);
		return "redirect:/admin/login";
	}
}

