package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.dto.LessonStatsWithInfoDto;
import ec.com.model.entity.Admin;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonListController {

    @Autowired
    private LessonService lessonService;

    // 講座一覧表示（売上・申込者数付き）
    @GetMapping("/admin/lesson/all")
    public String showLessonList(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        // 全講座の統計情報を取得（adminId 不使用）
        List<LessonStatsWithInfoDto> statsList = lessonService.getLessonStatsList();

        model.addAttribute("statsList", statsList);
        return "admin_lesson_lineup";
    }

    // キーワード検索（講座名 or 講座番号に部分一致）
    @GetMapping("/admin/lesson/search")
    public String searchLessonByKeyword(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        // 全講座の統計を取得して、キーワードで絞り込み
        List<LessonStatsWithInfoDto> allStats = lessonService.getLessonStatsList();

        List<LessonStatsWithInfoDto> filteredStats = allStats.stream()
            .filter(stat -> stat.getLessonName() != null && stat.getLessonName().contains(keyword)
                         || String.valueOf(stat.getLessonId()).contains(keyword))
            .toList();

        model.addAttribute("statsList", filteredStats);
        return "admin_lesson_lineup";
    }
}

