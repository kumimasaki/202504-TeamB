package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;

@Controller
public class AdminLessonDeleteController {
	@Autowired
	private LessonService lessonService;

	// GET /admin/lesson/delete
	@GetMapping("/admin/delete/lesson")
	public String getLessonDeletePage(Model model) {
		// 全ての講座の統計情報を取得
		model.addAttribute("statsList", lessonService.getLessonStatsList());
		// 画面表示する
		return "admin_delete_lesson";
	}

	// POST /admin/lesson/delete/remove

	@PostMapping("/admin/lesson/delete/remove")
	public String lessonDelet(@RequestParam Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		// lessonは存在しない
		if (lesson == null) {
			// IDが見つからなかった場合、エラーメッセージと共に画面に戻る
			model.addAttribute("error", "指定された講座は存在しません。");
			model.addAttribute("statsList", lessonService.getLessonStatsList());
			return "admin_delete_lesson";
		}
		// もし購入歴史があれば、削除できません
		boolean hasTransaction = lessonService.hasTransaction(lessonId);
		if (hasTransaction) {
			model.addAttribute("error", "この講座は既に購入されているため、削除できません。");
			model.addAttribute("statsList", lessonService.getLessonStatsList());
			return "admin_delete_lesson";
		}

		lessonService.deletByLesson(lessonId);
		return "admin_fix_delete"; // 削除完了画面に移動する

	}

}
