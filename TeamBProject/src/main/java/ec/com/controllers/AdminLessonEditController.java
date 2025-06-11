package ec.com.controllers;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.text.SimpleDateFormat;
//import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminLessonEditController {

	@Autowired
	private LessonService lessonService;

	@Autowired
	private HttpSession session;

	/*
	 * レッスン編集画面を表示する
	 * 
	 * @param lessonId 編集対象レッスンID (URLパスから取得)
	 * 
	 * @param model Viewに渡すデータ保持オブジェクト
	 * 
	 * @return String 遷移先画面 存在しないIDの場合: /admin/lesson/allへredirect
	 * 正常時:admin_edit_lesson.html
	 */
	// GET：編集画面の表示
	@GetMapping("/admin/lesson/edit/{lessonId}")
	public String showEditForm(@PathVariable Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}
		model.addAttribute("lesson", lesson);
		return "admin_edit_lesson";
	}

	/*
	 * レッスン情報を更新する
	 * 
	 * @param lesson フォームから送信された更新データ
	 * 
	 * @return String 遷移先画面（常に/admin/lesson/allへredirect）
	 */
	// POST：変更を保存
	@PostMapping("/admin/lesson/edit/update")
	public String updateLesson(@ModelAttribute Lesson lesson) {
		lessonService.updateLesson(lesson);
		return "redirect:/admin/lesson/all";
	}

	// GET ：/admin/lesson/image/edit/{lessonId}
	@GetMapping("/admin/lesson/image/edit/{lessonId}")
	public String getAdminLessonImageEditPage(@PathVariable Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);

		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		} else {
			model.addAttribute("lesson", lesson);
			return "redirect:/admin/lesson/image/edit/update";
		}
	}

	// POST :/admin/lesson/image/edit/update
//	@PostMapping("/admin/lesson/image/edit/update")
//	public String updateLessonImage(@RequestParam MultipartFile imageFile, @RequestParam Long lessonId, Model model) {
//		// lessonService.updateLesson(lesson);
//		Lesson lesson = lessonService.findById(lessonId);
//		if (lesson == null) {
//			return "redirect:/admin/lesson/all";
//		} else {
//			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())
//					+ imageFile.getOriginalFilename();
//			try {
//				Files.copy(imageFile.getInputStream(), Path.of("src/main/resources/static/lesson-image/" + fileName));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			// もし、同じfileの名前がなかったら保存
//			// レッスン編集画面redirect
//			if (lessonService.updateLesson(lesson)) {
//				return "redirect:/admin/lesson/edit/update";
//			} else {
//				// そうでない 画像編集画面に止まり
//				return "redirect:/admin/lesson/image/edit/update";
//			}

//		}

}
