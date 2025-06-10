package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.dao.UserDao;
import ec.com.model.entity.User;
import ec.com.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserLoginController {

	// httpセッションの宣言
	@Autowired
	private HttpSession session;

	@Autowired
	private UserService userService;

	// ログイン画面の表示
	@GetMapping("/user/login")
	public String getuserLoginPage() {
		return "user_login.html";
	}

	// ログイン処理
	@PostMapping("/user/login/process")
	public String userLoginProcess(@RequestParam String userEmail, @RequestParam String userPassword, Model model) {

		User user = userService.loginUser(userEmail, userPassword);
		if (user == null) {
			// メールとパスワードが一致しなければ(null),ログイン画面へ
			model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
			return "user_login.html";
			// 一致すればログイン成功、商品一覧画面へ
		} else {
			// ログイン情報を渡す
			session.setAttribute("loginUserInfo", user);
			return "user_menu.html";
		}
	}

}
