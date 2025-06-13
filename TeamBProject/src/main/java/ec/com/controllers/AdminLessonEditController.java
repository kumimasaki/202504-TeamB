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

	// 編集内容保存
	@PostMapping("/admin/lesson/edit/update")
	public String updateLesson(@ModelAttribute Lesson lesson, @RequestParam Long lessonId, Model model) {
		lessonService.updateLesson(lesson);
		/*
		 * 編集完了画面に移動する もし引き続き編集したい場合 完了画面から該当lessonに戻ることができる
		 */
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

	// 新規講座登録（画像アップロードあり・日時付き名前で保存）
	@PostMapping("/admin/lesson/register")
	public String registerLesson(@RequestParam("imageName") MultipartFile imageFile,
			@RequestParam("startDate") String startDate, @RequestParam("startTime") String startTime,
			@RequestParam("finishTime") String finishTime, @RequestParam("lessonName") String lessonName,
			@RequestParam("lessonDetail") String lessonDetail, @RequestParam("lessonFee") String lessonFee,
			@RequestParam("id") Long lessonId, Model model) {

		Lesson lesson = new Lesson();

		// 画像ファイル保存
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
				return "redirect:/admin/lesson/register?error=true";
			}
		}

		// 各項目セット
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

		// 管理者情報をセット
		var admin = (ec.com.model.entity.Admin) session.getAttribute("AdminLogin");
		if (admin != null) {
			lesson.setAdminId(admin.getAdminId());
		}

		lessonService.insertLesson(lesson);
		return "redirect:/admin/lesson/all";
	}

}
