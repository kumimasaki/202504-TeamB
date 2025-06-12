package ec.com.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam("imageName") MultipartFile imageFile) throws IOException {

        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        Lesson lesson = new Lesson();

        // 画像処理
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalFileName = imageFile.getOriginalFilename();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String newFileName = timestamp + "_" + originalFileName;

            Path saveDir = Paths.get("src/main/resources/static/lesson-image");
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }
            Path savePath = saveDir.resolve(newFileName);
            Files.copy(imageFile.getInputStream(), savePath);

            lesson.setImageName(newFileName);
        }

        // その他の項目をセット
        lesson.setStartDate(startDate);
        lesson.setStartTime(startTime);
        lesson.setFinishTime(finishTime);
        lesson.setLessonName(lessonName);
        lesson.setLessonDetail(lessonDetail);
        lesson.setLessonFee(lessonFee);
        lesson.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        lesson.setAdminId(admin.getAdminId());

        lessonService.registerLesson(lesson);

        return "redirect:/admin/lesson/all";
    }
}

