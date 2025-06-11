package ec.com.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ec.com.model.dao.LessonDao;
import ec.com.model.entity.Lesson;
import ec.com.model.entity.User;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー向け講座コントローラークラス 講座一覧表示機能を処理する
 */
@Controller
public class UserLessonController {
	/**
	 * HTTPセッションの依存性注入
	 */
	@Autowired
	private HttpSession session;
    //講座DAOの依存性注入
	@Autowired
	private LessonDao lessonDao;

	/**
	 * 講座一覧画面表示メソッド（メニュー画面） 当日以降の講座のみ表示する URL: GET /lesson/menu 機能:
	 * 全ての講座を取得し、講座一覧画面を表示する ログイン後にユーザーが購入可能な講座一覧を表示
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

		// 【修正】当日以降の講座のみ取得
		LocalDate today = LocalDate.now();
		List<Lesson> lessonList = lessonDao.findByStartDateGreaterThanEqual(today);
		model.addAttribute("lessonList", lessonList);

		return "user_menu.html";
	}

	/**
	 * 講座詳細画面表示メソッド
	 *
	 * URL: GET /lesson/detail/{lessonId} 機能: 指定された講座IDに基づき、講座の詳細情報を取得して詳細画面に表示する
	 * ログインフラグを判定し、画面に必要な情報をモデルに追加
	 *
	 * @param lessonId 表示対象の講座ID（パスパラメータ）
	 * @param session  HTTPセッション（ログイン情報などを取得）
	 * @param model    Spring MVCのModelオブジェクト（画面へデータを渡す）
	 * @return String 遷移先ページのファイル名（user_lesson_detail.html）
	 */
	@GetMapping("/lesson/detail/{lessonId}")
	public String getMethodName(@PathVariable("lessonId") Long lessonId, HttpSession session, Model model) {
		Lesson lesson = lessonDao.findByLessonId(lessonId);
		model.addAttribute("lesson", lesson);
		Boolean loginFlg = (Boolean) session.getAttribute("loginFlg");
		loginFlg = true; /* テスト */
		model.addAttribute("loginFlg", loginFlg);
		return "user_lesson_detail.html";
	}

	/**
	 * 【新規追加】ログアウト機能 URL: GET /lesson/menu/logout
	 */
	@GetMapping("/lesson/menu/logout")
	public String logout() {
		// セッションを無効化
		session.invalidate();

		// ログイン画面にリダイレクト
		return "redirect:/user/login";
	}

	/**
	 * 【カート追加処理】 URL: POST /lesson/cart/all 機能：レッスンをセッション内のカートリストに追加する。 -
	 * すでに追加されている場合はメッセージで通知。 - 追加成功時もメッセージで通知。
	 * 
	 * @param lessonId 追加対象のレッスンID
	 * @param session  HTTPセッション
	 * @return 処理結果のメッセージ（文字列）
	 */
	@PostMapping("/lesson/cart/all")
	@ResponseBody
	public String AddCart(@RequestParam("lessonId") Long lessonId, HttpSession session) {
		List<Lesson> list = (List<Lesson>) session.getAttribute("list");
		if (list == null) {
			list = new ArrayList<Lesson>();
			Lesson lesson = lessonDao.findByLessonId(lessonId);
			list.add(lesson);
			session.setAttribute("list", list);
			return "✅ レッスンをカートに追加しました！";
		}
		for (Lesson lesson : list) {
			if (lesson.getLessonId() == lessonId) {
				return "⚠️ このレッスンはすでにカートに追加されています。";
			}
		}
		Lesson lesson = lessonDao.findByLessonId(lessonId);
		list.add(lesson);
		session.setAttribute("list", list);
		return "✅ レッスンをカートに追加しました！";
	}
	/** 
 	一覧画面の表示
	セッションからカートの内容を取得、表示
	*/
	@GetMapping("/lesson/show/cart")
	public String getLessonShowCart(HttpSession session, Model model) {
	    // ログインチェック
	    User loginUser = (User) session.getAttribute("loginUserInfo");
	    // 未ログインならログイン画面へ
	    if (loginUser == null) {
	        return "/user/login.html"; 
	    }
	    // ログイン済みの場合
	    Boolean loginFlg = (Boolean) session.getAttribute("loginFlg");
	    if (loginFlg == null) {
	        loginFlg = true;
	    }
	    List<Lesson> list = (List<Lesson>) session.getAttribute("list");
	    if (list == null) {
	        list = new ArrayList<>();
	    }
	    model.addAttribute("loginFlg", loginFlg);
	    model.addAttribute("list", list);
	    return "user_planned_application.html";
	}

}