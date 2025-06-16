package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ec.com.model.dao.UserDao;
import ec.com.model.entity.Lesson;
import ec.com.model.entity.User;
import ec.com.services.UserService;
//import ec.com.services.UserService;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー登録コントローラークラス ユーザー登録に関するHTTPリクエストを処理する
 */
@Controller
public class UserRegisterController {

	/**
	 * ユーザーサービスの依存性注入 Springが自動的にUserServiceインスタンスを注入する
	 */
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;

	/**
	 * ユーザー登録画面表示メソッド
	 * 
	 * URL: GET /user/register 機能: ユーザー登録フォーム画面を表示する
	 * 
	 * @return user_register.html 登録画面のHTMLファイル名
	 */
	@GetMapping("/user/register")
	public String getUserRegisterPage() {
		return "user_register.html";
	}

	
	// 登録内容の確認処理
	@PostMapping("/user/confirm/process")
	public String userRegisterProcess(@RequestParam String userName, @RequestParam String userEmail,
			@RequestParam String userPassword, @RequestParam String confirmPassword, Model model) {
		if(userPassword.equals(confirmPassword)) {
			//確認のため登録内容を渡す
			model.addAttribute("userName",userName);
			model.addAttribute("userEmail",userEmail);
			model.addAttribute("userPassword",userPassword);
			// 確認画面へ遷移
			return "user_confirm_register.html";
		}else {
			model.addAttribute("userName",userName);
			model.addAttribute("userEmail",userEmail);
			model.addAttribute("registerError","パスワードが一致しません");
			return "user_Register.html";
		}
			
		}
	
	// ユーザー存在するかをチェックする
	@GetMapping("/user/check")
	@ResponseBody
	public boolean checkEmail(@RequestParam String email) {
		User user = userDao.findByUserEmail(email);
		
		if(user==null) {
			return true;
		}else {
			return false;
		}
	}
}
