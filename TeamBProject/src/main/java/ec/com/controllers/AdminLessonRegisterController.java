package ec.com.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ec.com.model.dao.LessonDao;
import ec.com.model.entity.Admin;
import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonRegisterController {

	@Autowired
	private LessonService lessonService;
	
	@Autowired
    private HttpSession session;
	
	@GetMapping("/admin/lesson/register")
	public String showRegisterForm(HttpSession session) {
	    Admin admin = (Admin) session.getAttribute("AdminLogin");
	    if (admin == null) {
	        return "redirect:/admin/login";
	    }
		return "admin_register_lesson.html";
	}

	@PostMapping("/admin/lesson/register/create")
	public String registerLesson(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
			@RequestParam("finishTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime finishTime,
			@RequestParam("lessonName") String lessonName,
			@RequestParam("lessonDetail") String lessonDetail,
			@RequestParam("lessonFee") Integer lessonFee,
			@RequestParam("imageName") MultipartFile imageFile,
			HttpSession session) throws IOException {

		// ファイル名を取得
		String imageName = imageFile.getOriginalFilename();

		// classpath の static/img フォルダを取得
		File imgDir = new ClassPathResource("static/img").getFile();

		// 保存先のファイルを作成
		File saveFile = new File(imgDir, imageName);

		// ファイルを保存
		imageFile.transferTo(saveFile);

		// adminId をセッションから取得
		 Admin admin = (Admin) session.getAttribute("AdminLogin");
		    if (admin == null) {
		        return "redirect:/admin/login";
		    }
		    Long adminId = admin.getAdminId(); 

		// Lesson エンティティにセット
		Lesson lesson = new Lesson();
		lesson.setStartDate(startDate);
		lesson.setStartTime(startTime);
		lesson.setFinishTime(finishTime);
		lesson.setLessonName(lessonName);
		lesson.setLessonDetail(lessonDetail);
		lesson.setLessonFee(lessonFee);
		lesson.setImageName(imageName);
		lesson.setRegisterDate(new Timestamp(System.currentTimeMillis()));
		lesson.setAdminId(adminId);

		// DB 保存
		lessonService.registerLesson(lesson);
		
		// 一覧画面へリダイレクト
		return "redirect:/admin/lesson/all";
	}
}


