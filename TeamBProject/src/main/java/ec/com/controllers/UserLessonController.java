package ec.com.controllers;

import java.time.LocalDate;  // 添加这个import
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ec.com.model.dao.LessonDao;
import ec.com.model.entity.Lesson;
import ec.com.model.entity.User;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserLessonController {

    @Autowired
    private HttpSession session;

    @Autowired
    private LessonDao lessonDao;

    /**
     * 講座一覧画面表示メソッド（メニュー画面）
     * 当日以降の講座のみ表示する
     */
    @GetMapping("/lesson/menu")
    public String getLessonMenuPage(Model model) {
        // ログイン状態をチェック
        User loginUser = (User) session.getAttribute("loginUserInfo");

        if (loginUser != null) {
            // ログイン済みの場合
            model.addAttribute("loginFlg", true);
            model.addAttribute("userName", loginUser.getUserName());
        } else {
            // 未ログインの場合
            model.addAttribute("loginFlg", false);
        }

        // 【修正】当日以降の講座のみ取得
        LocalDate today = LocalDate.now();
        List<Lesson> lessonList = lessonDao.findByStartDateGreaterThanEqual(today);
        model.addAttribute("lessonList", lessonList);

        return "user_menu.html";
    }

    @GetMapping("/lesson/detail/{lessonId}")
    public String getMethodName(@PathVariable("lessonId") Long lessonId,
                                HttpSession session,
                                Model model) {
        Lesson lesson = lessonDao.findByLessonId(lessonId);
        model.addAttribute("lesson", lesson);
        Boolean loginFlg = (Boolean)session.getAttribute("loginFlg");
        loginFlg = true; /*テスト*/
        model.addAttribute("loginFlg", loginFlg);
        return "user_lesson_detail.html";
    }

    /**
     * 【新規追加】ログアウト機能
     * URL: GET /lesson/menu/logout
     */
    @GetMapping("/lesson/menu/logout")
    public String logout() {
        // セッションを無効化
        session.invalidate();
        
        // ログイン画面にリダイレクト
        return "redirect:/user/login";
    }
}