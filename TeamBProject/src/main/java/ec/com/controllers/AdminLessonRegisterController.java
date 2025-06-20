package ec.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // 登録画面の表示
    @GetMapping("/admin/lesson/register")
    public String showRegisterForm(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        return "admin_register_lesson.html";
    }

    // 新規講座登録処理（DTOなしバージョン）
    @PostMapping("/admin/lesson/register")
    public String registerLesson(
            @RequestParam("imageName") MultipartFile imageFile,
            @RequestParam("startDate") String startDate,
            @RequestParam("startTime") String startTime,
            @RequestParam("finishTime") String finishTime,
            @RequestParam("lessonName") String lessonName,
            @RequestParam("lessonDetail") String lessonDetail,
            @RequestParam("lessonFee") String lessonFee,
            @RequestParam("capacity") String capacity,
            HttpSession session,
            Model model) {

        // 入力バリデーション（簡易）
        if (lessonName.isBlank() || lessonDetail.isBlank() || startDate.isBlank() ||
                startTime.isBlank() || finishTime.isBlank() || lessonFee.isBlank() || capacity.isBlank()) {
            model.addAttribute("registerError", "すべての項目を入力してください。");
            return "admin_register_lesson.html";
        }

        Lesson lesson = new Lesson();

        // 画像保存
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
        lesson.setStartDate(LocalDate.parse(startDate));
        lesson.setStartTime(LocalTime.parse(startTime));
        lesson.setFinishTime(LocalTime.parse(finishTime));
        lesson.setLessonName(lessonName);
        lesson.setLessonDetail(lessonDetail);
        lesson.setLessonFee(Integer.parseInt(lessonFee));
        lesson.setCapacity(Integer.parseInt(capacity));
        lesson.setRegisterDate(Timestamp.valueOf(LocalDateTime.now()));

        // DB登録
        lessonService.registerLesson(lesson);

        return "admin_fix_register";
    }
}

