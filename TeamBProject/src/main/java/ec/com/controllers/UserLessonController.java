package ec.com.controllers;

import java.time.LocalDateTime;
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
 * ユーザー向け講座管理コントローラークラス
 * 講座一覧表示、詳細表示、カート機能、セッション管理を担当
 * 
 */
@Controller
public class UserLessonController {
	
	/**
	 * HTTPセッション管理オブジェクト
	 * ユーザーのログイン状態やカート情報を管理
	 */
	@Autowired
	private HttpSession session;
	
	/**
	 * 講座データアクセスオブジェクト
	 * 講座情報の取得、検索などのデータベースアクセスを担当
	 */
	@Autowired
	private LessonDao lessonDao;

	/**
	 * 講座一覧画面表示メソッド（メニュー画面）
	 * 現在時刻以降の講座のみを取得し、時分まで精密チェックを行う
	 * 
	 * URL: GET /lesson/menu
	 * 
	 * @param model Spring MVCのModelオブジェクト（画面へのデータ受け渡し用）
	 * @return String 遷移先画面のテンプレートファイル名 (user_menu.html)
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

		// 現在時刻以降の講座のみ取得
		LocalDateTime currentDateTime = LocalDateTime.now();
		List<Lesson> lessonList = lessonDao.findUpcomingLessons(currentDateTime);
		model.addAttribute("lessonList", lessonList);

		return "user_menu.html";
	}

	/**
	 * 講座詳細画面表示メソッド
	 * 指定された講座IDに基づいて詳細情報を取得し表示
	 * ログイン状態を厳密にチェックし、適切なフラグを設定
	 * 
	 * URL: GET /lesson/detail/{lessonId}
	 * 
	 * @param lessonId 表示対象の講座ID（パスパラメータから取得）
	 * @param session HTTPセッション（ログイン情報取得用）
	 * @param model Spring MVCのModelオブジェクト（画面へのデータ受け渡し用）
	 * @return String 遷移先画面のテンプレートファイル名 (user_lesson_detail.html)
	 */
	@GetMapping("/lesson/detail/{lessonId}")
	public String getMethodName(@PathVariable("lessonId") Long lessonId, HttpSession session, Model model) {
		Lesson lesson = lessonDao.findByLessonId(lessonId);
		model.addAttribute("lesson", lesson);
		Boolean loginFlg = (Boolean) session.getAttribute("loginFlg");
		if (loginFlg == null || !loginFlg) {
			loginFlg = false;
		} else {
			loginFlg = true;
		}
		model.addAttribute("loginFlg", loginFlg);
		return "user_lesson_detail.html";
	}

	/**
	 * ユーザーログアウト処理メソッド
	 * HTTPセッションを無効化し、ログイン画面にリダイレクト
	 * 
	 * URL: GET /lesson/menu/logout
	 * 
	 * @return String リダイレクト先URL (redirect:/user/login)
	 */
	@GetMapping("/lesson/menu/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/login";
	}

	/**
	 * カート追加処理メソッド
	 * 指定された講座をセッション内のカートリストに追加
	 * ログイン必須：未ログインの場合は処理を拒否
	 * 
	 * URL: POST /lesson/cart/all
	 * 
	 * @param lessonId 追加対象の講座ID（リクエストパラメータから取得）
	 * @param session HTTPセッション（ログイン情報・カート情報の保存・取得用）
	 * @return String 処理結果のメッセージ
	 *               成功: "✅ レッスンをカートに追加しました！"
	 *               重複: "⚠️ このレッスンはすでにカートに追加されています。"
	 *               未ログイン: "refuse"
	 */
	@PostMapping("/lesson/cart/all")
	@ResponseBody
	public String AddCart(@RequestParam("lessonId") Long lessonId, HttpSession session) {
		User user = (User) session.getAttribute("loginUserInfo");
		if (user == null) {
			return "refuse";
		}
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
	 * カート一覧画面表示メソッド
	 * セッションに保存されているカート内容を取得し表示
	 * ログイン必須機能：未ログインの場合はログイン画面にリダイレクト
	 * 
	 * URL: GET /lesson/show/cart
	 * 
	 * @param session HTTPセッション（ログイン情報・カート情報取得用）
	 * @param model Spring MVCのModelオブジェクト（画面へのデータ受け渡し用）
	 * @return String 遷移先画面のテンプレートファイル名
	 *               ログイン済み: user_planned_application.html
	 *               未ログイン: /user/login.html
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