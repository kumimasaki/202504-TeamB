package ec.com.controllers;

import ec.com.model.entity.Admin;
import ec.com.service.AdminService;
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

    // 管理者ログイン画面を表示
    @GetMapping("/admin/login")
    public String showLoginForm(@RequestParam(name = "error", required = false) String error,
                                 Model model) {
        if (error != null) {
            model.addAttribute("error", "メールアドレスまたはパスワードが間違っています。");
        }
        return "admin_login.html";
    }

    // ログイン処理
    @PostMapping("/admin/login")
    public String loginProcess(@RequestParam String adminEmail,
                               @RequestParam String password) {

        Admin admin = adminService.loginCheck(adminEmail, password);

        if (admin == null) {
            return "redirect:/admin/login?error=true";
        }

        adminService.loginSuccess(session, admin);
        return "redirect:/admin/register";  // ※成功后跳转页面可按需调整
    }

    // ログアウト処理
    @GetMapping("/admin/logout")
    public String logout() {
        adminService.logout(session);
        return "redirect:/admin/login";
    }
}

