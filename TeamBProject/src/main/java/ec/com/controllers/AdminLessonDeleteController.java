package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonDeleteController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	private HttpSession session;
	// GET /admin/lesson/delete

	@GetMapping("/admin/delete/lesson")
	public String getLessonDeletePage(Model model) {
		// 登録した講座全て表示する
		model.addAttribute("lessonList", lessonService.findAll());
		// 画面表示する
		return "admin_delete_lesson";
	}

	// POST /admin/lesson/delete/remove

	@PostMapping("/admin/lesson/delete/remove")
	public String lessonDelet(@RequestParam Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		// もしlessonはnullではない、削除する
		if (lesson != null) {
			lessonService.deletByLesson(lessonId); // 删除处理
			return "admin_fix_delete"; // 削除完了画面に移動する
		} else {
			return "admin_lesson_delete";
		}
	}

}
