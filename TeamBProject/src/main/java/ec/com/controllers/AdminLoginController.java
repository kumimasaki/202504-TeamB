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

	@GetMapping("/admin/login")
	public String showLoginForm(@RequestParam(name = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "メールアドレスまたはパスワードが間違っています。");
		}
		return "admin_login";  
	}

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

	@GetMapping("/admin/logout")
	public String logout() {
		adminService.logout(session);
		return "redirect:/admin/login";
	}

}
