package ec.com.controllers;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminLessonEditController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private HttpSession session;

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

    // POST：変更を保存
    @PostMapping("/admin/lesson/edit/update")
    public String updateLesson(@ModelAttribute Lesson lesson) {
        lessonService.updateLesson(lesson);  
        return "redirect:/admin/lesson/all";
    }
}
