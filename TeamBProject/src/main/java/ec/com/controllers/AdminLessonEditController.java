package ec.com.controllers;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

	// 編集内容保存Add commentMore actions
	@PostMapping("/admin/lesson/edit/update")
	public String updateLesson(@ModelAttribute Lesson lesson, @RequestParam Long lessonId, Model model) {

		// 🔍 開始日時チェックのために値を取得
		LocalDate startDate = lesson.getStartDate();
		LocalTime startTime = lesson.getStartTime();
		LocalTime finishTime = lesson.getFinishTime();

		// 🕒 現在時刻
		LocalDateTime now = LocalDateTime.now();

		// ⏰ 日時チェック（すべての値が揃ってる時のみ実行）
		if (startDate != null && startTime != null && finishTime != null) {

			// ① 開始日時が現在より前
			LocalDateTime lessonStartDateTime = LocalDateTime.of(startDate, startTime);
			if (lessonStartDateTime.isBefore(now)) {
				model.addAttribute("editError", "開始日時は現在より後にしてください。");
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson";
			}

			// ② 開始日が今日より前
			if (startDate.isBefore(now.toLocalDate())) {
				model.addAttribute("editError", "開始日は今日以降にしてください。");
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson";
			}

			// ③ 開始時間 > 終了時間
			if (startTime.isAfter(finishTime)) {
				model.addAttribute("editError", "開始時間は終了時間より前にしてください。");
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson";
			}
		}

		// 更新実行
		lessonService.updateLesson(lesson);

		// 完了画面に lessonId を渡す（画面に再編集リンクがある場合など）
		model.addAttribute("lessonId", lesson.getLessonId());
		return "admin_fix_edit";
	}

	// 画像変更ページを表示
	@GetMapping("/admin/lesson/image/edit/{lessonId}")
	public String getAdminLessonImageEditPage(@PathVariable Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}
		model.addAttribute("lessonList", lesson); // admin_edit_lesson_img.html 用
		return "admin_edit_lesson_img";
	}

	// 画像アップロード処理（ファイル保存あり・日時付き名前で保存）
	@PostMapping("/admin/lesson/image/edit/update")
	public String updateLessonImage(@RequestParam("imageName") MultipartFile imageFile,
			@RequestParam("lessonId") Long lessonId, @RequestParam("startDate") String startDate,
			@RequestParam("startTime") String startTime, @RequestParam("finishTime") String finishTime,
			@RequestParam("lessonName") String lessonName, @RequestParam("lessonDetail") String lessonDetail,
			@RequestParam("lessonFee") String lessonFee) {

		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}

		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				String originalFileName = imageFile.getOriginalFilename();
				String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
				String newFileName = timestamp + "_" + originalFileName;
				Path savePath = Paths.get("src/main/resources/static/lesson-image/" + newFileName);
				Files.copy(imageFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
				lesson.setImageName(newFileName);
			} catch (IOException e) {
				e.printStackTrace();
				return "redirect:/admin/lesson/image/edit/" + lessonId + "?error=true";
			}
		}

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

