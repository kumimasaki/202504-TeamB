package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.entity.User;
import ec.com.services.UserService;

@Controller
public class UserChangePasswordController {

	@Autowired
	private UserService userService;

	// 画面の表示
	@GetMapping("/user/change/password")
	public String getChangePasswordPage() {
		return "user_password_change.html";
	}

	// パスワード変更処理
	@PostMapping("/user/change/password/process")
	public String ChangePasswordComplete(@RequestParam String userEmail, @RequestParam String userPassword,
			@RequestParam String passwordConfirm, Model model) {
		// 渡されたEmailを検索
		User user = userService.findByUserEmail(userEmail);
		// Emailが存在すれば処理を続行
		if (user != null) {
			if (userPassword.equals(passwordConfirm)) {
				// 成功時、パスワードの変更処理を行いログイン画面へ
				user.setUserPassword(userPassword);
				userService.updateUser(user);
				return "user_change_password_complete.html";
			} else {
				// 失敗時
				model.addAttribute("passErr", "パスワードが一致しません");
				model.addAttribute("userEmail", userEmail);
				return "user_password_change.html";
			}
		} else {
			// 存在しない場合メール入力画面へ
			return "user_password_reset";
		}
	}
	// パスワード変更完了画面の表示
	@GetMapping("/user/change/password/complete")
	public String getChangePasswordCompletePage() {
		return "user_change_password_complete.html";
	}

}
