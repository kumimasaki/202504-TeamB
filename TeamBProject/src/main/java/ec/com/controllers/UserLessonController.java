package ec.com.controllers;

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

/**
 * ユーザー向け講座コントローラークラス
 * 講座一覧表示機能を処理する
 */
@Controller
public class UserLessonController {

    /**
     * HTTPセッションの依存性注入
     */
    @Autowired
    private HttpSession session;

    /**
     * 講座DAOの依存性注入
     */
    @Autowired
    private LessonDao lessonDao;

    /**
     * 講座一覧画面表示メソッド（メニュー画面）
     * 
     * URL: GET /lesson/menu
     * 機能: 全ての講座を取得し、講座一覧画面を表示する
     *      ログイン後にユーザーが購入可能な講座一覧を表示
     * 
     * @param model Spring MVCのModelオブジェクト
     * @return String 遷移先ページのファイル名
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

        // 全講座を取得してモデルに追加
        List<Lesson> lessonList = lessonDao.findAll();
        model.addAttribute("lessonList", lessonList);

        // user_menu.html テンプレートを返す
        return "user_menu.html";
    }
    
    /**
     * 講座詳細画面表示メソッド
     * 
     * URL: GET /lesson/detail/{lessonId}
     * 機能: 指定された講座IDに基づき、講座の詳細情報を取得して詳細画面に表示する
     *      ログインフラグを判定し、画面に必要な情報をモデルに追加
     * 
     * @param lessonId 表示対象の講座ID（パスパラメータ）
     * @param session HTTPセッション（ログイン情報などを取得）
     * @param model Spring MVCのModelオブジェクト（画面へデータを渡す）
     * @return String 遷移先ページのファイル名（user_lesson_detail.html）
     */
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
}