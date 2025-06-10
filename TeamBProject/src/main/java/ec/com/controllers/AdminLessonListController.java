package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ec.com.model.entity.Admin;
import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonListController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/admin/lesson/all")
    public String showLessonList(Model model, HttpSession session) {
        // セッションからadminIdを取得
    	Admin admin = (Admin) session.getAttribute("AdminLogin");
    	if (admin == null) {
    	    return "redirect:/admin/login";
    	}
    	Long adminId = admin.getAdminId();

        // adminIdがセッションにない場合はログイン画面にリダイレクト
        if (adminId == null) {
            return "redirect:/admin/login";
        }

        // 管理者IDに紐づく講座一覧を取得
        List<Lesson> lessonList = lessonService.selectAllLessonList(adminId);
        model.addAttribute("lessonList", lessonList);
        return "admin_lesson_lineup";
    }
}

