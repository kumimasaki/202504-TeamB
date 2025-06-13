package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/admin/lesson/search")
    public String searchLessonByKeyword(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
    	// 管理者ログイン情報をセッションから取得
    	Admin admin = (Admin) session.getAttribute("AdminLogin");
    	if (admin == null) {
    		// ログインしていない場合、ログイン画面にリダイレクト
    		return "redirect:/admin/login";
    	}

    	// サービス経由で講座名に部分一致する講座一覧を取得
    	List<Lesson> lessonList = lessonService.searchLessonByKeyword(admin.getAdminId(), keyword);

    	// 画面に表示する講座リストをセット
    	model.addAttribute("lessonList", lessonList);

    	// 元の講座一覧画面に戻る
    	return "admin_lesson_lineup";
}
}



