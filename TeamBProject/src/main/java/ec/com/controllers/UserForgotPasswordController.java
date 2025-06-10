package ec.com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserForgotPasswordController {
	
	// 画面の表示
	@GetMapping("/user/password/reset")
	public String getuserLoginPage() {
		return "user_password_reset.html";
	}
	
	// パスワードを変更したいメールの入力処理
	@PostMapping("/user/password/reset/mail")
	public String userPasswordReset(@RequestParam String userEmail, Model model) {
		model.addAttribute("userEmail", userEmail);
		return "user_password_change.html";
	}
}
