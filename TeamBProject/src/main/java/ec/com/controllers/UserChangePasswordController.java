package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.services.UserService;

@Controller
public class UserChangePasswordController {
	
	@Autowired
	private UserService userService;
	// 画面の表示
	@GetMapping ("/user/password/change")
	public String getChangePasswordPage() {
		return"user_password_change.html";
	}
	
	//パスワード変更処理
//	@PostMapping("/user/change/password/complete")
//	public String ChangePasswordComplete(@RequestParam String userEmail, @RequestParam String userPassword,
//			@RequestParam passwordConfirm, Model model) {
//		
//	}
}

