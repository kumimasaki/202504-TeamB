package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.dao.UserDao;
import ec.com.model.entity.User;

@Controller
public class UserForgotPasswordController {
	
	@Autowired
	private UserDao userDao;
	
	// 画面の表示
	@GetMapping("/user/password/reset")
	public String getuserLoginPage() {
		return "user_password_reset.html";
	}
	
	// パスワードを変更したいメールの入力処理
	@PostMapping("/user/password/reset/mail")
	public String userPasswordReset(@RequestParam String userEmail, Model model) {
		//メールアドレスの取得
		User user = userDao.findByUserEmail(userEmail);
		if(user != null) {
			//存在する場合
			model.addAttribute("userEmail", userEmail);
			return "user_password_change.html";
		}else {
			//存在しない場合
			return "redirect:/user/password/reset";
		}
		
	}
}
