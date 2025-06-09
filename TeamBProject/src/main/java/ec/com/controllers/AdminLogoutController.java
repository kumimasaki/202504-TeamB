package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.dao.AdminDao;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLogoutController {
	// DAOを自動注入（DB操作用）
		@Autowired
		private AdminDao adminDao;
		
		// ログアウト処理
		@GetMapping("/user/logout")
		public String getLogout(HttpSession session) {
			// sessionをクリアする
			session.invalidate();
			return "login.html";
		}
}
