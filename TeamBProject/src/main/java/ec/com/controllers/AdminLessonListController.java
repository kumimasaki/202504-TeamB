package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.dto.LessonStatsWithInfoDto;
import ec.com.model.entity.Admin;
import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonListController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/admin/lesson/all")
    public String showLessonList(Model model, HttpSession session) {
        // セッションからadminIdを取得
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        Long adminId = admin.getAdminId();

        // 管理者IDに紐づく講座一覧を取得
        List<Lesson> lessonList = lessonService.selectAllLessonList(adminId);
        // 売上・申込者数の情報も追加で取得（追加）
        List<LessonStatsWithInfoDto> statsList = lessonService.getLessonStatsList(adminId);

        // モデルに詰めて画面へ渡す
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("statsList", statsList); // 追加

        return "admin_lesson_lineup";
    }

    @GetMapping("/admin/lesson/search")
    public String searchLessonByKeyword(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        // 管理者ログイン情報をセッションから取得
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        Long adminId = admin.getAdminId();

        // サービス経由で講座名に部分一致する講座一覧を取得
        List<Lesson> lessonList = lessonService.searchLessonByKeyword(adminId, keyword);
        // 売上・申込者数の情報も追加で取得（追加）
        List<LessonStatsWithInfoDto> statsList = lessonService.getLessonStatsList(adminId);

        // 画面に表示する講座リスト＆集計結果をセット
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("statsList", statsList); // 追加

        return "admin_lesson_lineup";
    }
}

