package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ec.com.model.dao.AdminDao;
import ec.com.model.entity.Admin;
import ec.com.services.AdminService;

//これがHTMLページを返すControllerであることを示すもの。
@Controller
public class AdminRegisterController {
	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminDao adminDao;

	// 登録画面表示 @GetMapping("/admin/register")
	@GetMapping("/admin/register")
	public String getAdminRegister() {
		return "admin_register.html";
	}

	/**
	 * ユーザー登録処理メソッド
	 * 
	 * URL: POST /admin/confirm 機能: 登録フォームから送信されたデータを確認する
	 * 
	 * @param adminName  管理者名（HTMLフォームから送信）
	 * @param adminEmail 管理者メールアドレス（HTMLフォームから送信）
	 * @param password   パスワード（HTMLフォームから送信）
	 * @param adminpassword    パスワード（HTMLフォームから送信）
	 * @return String 遷移先ページのファイル名 失敗時：admin_register.html（登録画面に留まる）
	 *         成功時：admin_confirm_register.html（ログイン画面に遷移）
	 */

	// 登録確認 @PostMapping("/admin/confirm")
	@PostMapping("admin/confirm")
	public String adminRegisterConfirm(@RequestParam String adminName, @RequestParam String adminEmail,
			@RequestParam String password, @RequestParam String adminPassword, Model model) {

		// パスワード一致チェック
		if (!password.equals(adminPassword)) {
			model.addAttribute("error", "パスワードが一致しません。");
			// 元の画面に戻る
			return "admin_register.html";
		} else {
			model.addAttribute("adminName", adminName);
			model.addAttribute("adminEmail", adminEmail);
			model.addAttribute("adminPassword", adminPassword);
			return "admin_confirm_register.html";
		}

	}

	// 登録処理 @PostMapping("/admin/register/process")
	@PostMapping("/admin/register/process")
	public String adminRegisterProcess(@RequestParam String adminName, @RequestParam String adminEmail,
			@RequestParam String adminPassword, Model model) {
		// adiminを作成したら、ログイン画面へ移動
		if (adminService.createAdmin(adminName, adminEmail, adminPassword)) {
			return "redirect:/admin/login";
		} else {
			// そうでない、登録画面に戻る
			return "redirect:/admin/register";
		}
	}

	// 管理者存在するかをチェックする
	@GetMapping("/admin/check")
	@ResponseBody
	public boolean checkAdminEmail(@RequestParam String email) {
		Admin admin = adminDao.findByAdminEmail(email);

		if (admin == null) {
			return true;
		} else {
			return false;
		}
	}
}
