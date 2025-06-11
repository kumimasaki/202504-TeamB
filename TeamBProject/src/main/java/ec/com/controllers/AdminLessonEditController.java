package ec.com.controllers;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class AdminLessonEditController {

	@Autowired
	private LessonService lessonService;

	@Autowired
	private HttpSession session;

	// 編集画面表示
	@GetMapping("/admin/lesson/edit/{lessonId}")
	public String showEditForm(@PathVariable Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}
		model.addAttribute("lesson", lesson);
		return "admin_edit_lesson";
	}

	// 編集内容保存
	@PostMapping("/admin/lesson/edit/update")
	public String updateLesson(@ModelAttribute Lesson lesson) {
		lessonService.updateLesson(lesson);
		return "redirect:/admin/lesson/all";
	}

	// 画像変更ページを表示
	@GetMapping("/admin/lesson/image/edit/{lessonId}")
	public String getAdminLessonImageEditPage(@PathVariable Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}
		model.addAttribute("lessonList", lesson);  // admin_edit_lesson_img.html 用
		return "admin_edit_lesson_img";
	}

	// 画像アップロード処理（ファイルコピーせず、名前だけ保存）
	@PostMapping("/admin/lesson/image/edit/update")
	public String updateLessonImage(@RequestParam("imageName") MultipartFile imageFile,
	                                @RequestParam("lessonId") Long lessonId,
	                                @RequestParam("startDate") String startDate,
	                                @RequestParam("startTime") String startTime,
	                                @RequestParam("finishTime") String finishTime,
	                                @RequestParam("lessonName") String lessonName,
	                                @RequestParam("lessonDetail") String lessonDetail,
	                                @RequestParam("lessonFee") String lessonFee) {

	    Lesson lesson = lessonService.findById(lessonId);
	    if (lesson == null) {
	        return "redirect:/admin/lesson/all";
	    }

	    // ファイル名だけ保存（コピー処理なし）
	    if (imageFile != null && !imageFile.isEmpty()) {
	        String fileName = imageFile.getOriginalFilename();
	        lesson.setImageName(fileName);
	    }

	    // データ変換とセット（空チェックあり）
	    if (startDate != null && !startDate.isBlank()) {
	        lesson.setStartDate(LocalDate.parse(startDate));
	    }
	    if (startTime != null && !startTime.isBlank()) {
	        lesson.setStartTime(LocalTime.parse(startTime));
	    }
	    if (finishTime != null && !finishTime.isBlank()) {
	        lesson.setFinishTime(LocalTime.parse(finishTime));
	    }
	    lesson.setLessonName(lessonName);
	    lesson.setLessonDetail(lessonDetail);
	    if (lessonFee != null && !lessonFee.isBlank()) {
	        lesson.setLessonFee(Integer.parseInt(lessonFee));
	    }

	    lessonService.updateLesson(lesson);
	    return "redirect:/admin/lesson/all";
	}
}
