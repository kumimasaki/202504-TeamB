package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import ec.com.model.entity.Admin;
import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonListController {
	@Autowired
	private HttpSession session;

	@Autowired
	private LessonService lessonService;

	// GET /admin/lesson/all

	@GetMapping("/admin/lesson/all")
	public String getAdminLessonAll(Model model) {
		// sessionからloginしてる人の情報をadminという変数に格納
		Admin admin = (Admin) session.getAttribute("AdminLogin");
		if (admin == null) {
			// もしadmin == nullなら、login画面にredirectする
			return "redirect:/admin/login";
		} else {
			// Lessonの情報を取得する
			List<Lesson> lessonList = lessonService.selectAllLessonList(admin.getAdminId());
			// model これを押して、講座allに飛ぶ
			model.addAttribute("lessonList", lessonList);
			return "admin_lesson_lineup.html";
		}
	}
}
