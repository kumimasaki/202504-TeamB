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
    private LessonService lessonService;  // Lessonの統計情報を取得するServiceをDI

    /**
     * 講座一覧表示（売上・申込者数付き）
     * 管理者がログインしている場合に全講座の統計情報を取得し、
     * ビュー「admin_lesson_lineup」にデータを渡して表示します。
     */
    @GetMapping("/admin/lesson/all")
    public String showLessonList(Model model, HttpSession session) {
        // セッションからAdminログイン情報を取得
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            // 未ログインならログイン画面へリダイレクト
            return "redirect:/admin/login";
        }

        // adminIdに関わらず、全講座の売上＆申込者数統計をServiceから取得
        List<LessonStatsWithInfoDto> statsList = lessonService.getLessonStatsList();

        // 取得した統計情報をモデルに追加
        model.addAttribute("statsList", statsList);

        // 講座一覧画面をレンダリング
        return "admin_lesson_lineup";
    }

    /**
     * キーワード検索（講座名 or 講座番号に部分一致）
     * 管理者がログインしている場合に、全講座の統計情報を一旦取得し、
     * 指定のキーワードで絞り込んだ結果を表示します。
     *
     * @param keyword 検索キーワード（講座名または講座IDの文字列）
     */
    @GetMapping("/admin/lesson/search")
    public String searchLessonByKeyword(
            @RequestParam("keyword") String keyword,
            Model model,
            HttpSession session) {

        // セッションからAdminログイン情報を取得
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            // 未ログインならログイン画面へリダイレクト
            return "redirect:/admin/login";
        }

        // 全講座の統計情報をServiceから取得
        List<LessonStatsWithInfoDto> allStats = lessonService.getLessonStatsList();

        // キーワードで絞り込み：講座名または講座IDを文字列変換して部分一致をチェック
        List<LessonStatsWithInfoDto> filteredStats = allStats.stream()
            .filter(stat ->
                // 講座名がnullでないかチェックしつつ、keywordを含むか判定
                (stat.getLessonName() != null && stat.getLessonName().contains(keyword))
                // 講座IDも文字列としてkeywordマッチを判定
                || String.valueOf(stat.getLessonId()).contains(keyword)
            )
            .toList();

        // 絞り込んだ統計情報をモデルに追加
        model.addAttribute("statsList", filteredStats);

        // 講座一覧画面を再レンダリング（検索結果表示）
        return "admin_lesson_lineup";
    }
}
