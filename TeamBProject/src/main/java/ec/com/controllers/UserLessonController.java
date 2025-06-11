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
 * ユーザー向け講座コントローラークラス 講座一覧表示機能を処理する
 */
@Controller
public class UserLessonController {
	/**
	 * HTTPセッションの依存性注入
	 */
	@Autowired
	private HttpSession session;
	// 講座DAOの依存性注入
	@Autowired
	private LessonDao lessonDao;

	/**
	 * 講座一覧画面表示メソッド（メニュー画面） 現在時刻以降の講座のみ表示する（時分まで精密チェック）
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

		//現在時刻以降の講座のみ取得
		LocalDateTime currentDateTime = LocalDateTime.now();
		List<Lesson> lessonList = lessonDao.findUpcomingLessons(currentDateTime);
		model.addAttribute("lessonList", lessonList);

		return "user_menu.html";
	}

	/**
	 * 講座詳細画面表示メソッド
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
	 * ログアウト機能
	 */
	@GetMapping("/lesson/menu/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/login";
	}

	/**
	 * カート追加処理
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
	 * カート一覧画面の表示
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