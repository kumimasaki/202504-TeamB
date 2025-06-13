package ec.com.controllers;

import java.io.Console;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import ec.com.model.dao.TransactionHistoryDao;
import ec.com.model.dao.TransactionItemDao;
import ec.com.model.dto.LessonWithTransactionDto;
import ec.com.model.entity.Lesson;
import ec.com.model.entity.TransactionHistory;
import ec.com.model.entity.TransactionItem;
import ec.com.model.entity.User;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー向け講座管理コントローラークラス 講座一覧表示、詳細表示、カート機能、セッション管理を担当
 * 
 */
@Controller
public class UserLessonController {

	/**
	 * HTTPセッション管理オブジェクト ユーザーのログイン状態やカート情報を管理
	 */
	@Autowired
	private HttpSession session;

	/**
	 * 講座データアクセスオブジェクト 講座情報の取得、検索などのデータベースアクセスを担当
	 */
	@Autowired
	private LessonDao lessonDao;

	@Autowired
	private TransactionHistoryDao transactionHistoryDao;

	@Autowired
	private TransactionItemDao transactionItemDao;

	@Autowired
	private LessonService lessonService;

	/**
	 * 講座一覧画面表示メソッド（メニュー画面） 現在時刻以降の講座のみを取得し、時分まで精密チェックを行う
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
	 * 講座詳細画面表示メソッド 指定された講座IDに基づいて詳細情報を取得し表示 ログイン状態を厳密にチェックし、適切なフラグを設定
	 * 
	 * URL: GET /lesson/detail/{lessonId}
	 * 
	 * @param lessonId 表示対象の講座ID（パスパラメータから取得）
	 * @param session  HTTPセッション（ログイン情報取得用）
	 * @param model    Spring MVCのModelオブジェクト（画面へのデータ受け渡し用）
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
	 * ユーザーログアウト処理メソッド HTTPセッションを無効化し、ログイン画面にリダイレクト
	 * 
	 * URL: GET /lesson/menu/logout
	 * 
	 * @return String リダイレクト先URL (redirect:/user/login)
	 */
	@GetMapping("/lesson/menu/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/lesson/menu";
	}

	/**
	 * カート追加処理メソッド 指定された講座をセッション内のカートリストに追加 ログイン必須：未ログインの場合は処理を拒否
	 * 
	 * URL: POST /lesson/cart/all
	 * 
	 * @param lessonId 追加対象の講座ID（リクエストパラメータから取得）
	 * @param session  HTTPセッション（ログイン情報・カート情報の保存・取得用）
	 * @return String 処理結果のメッセージ 成功: "✅ レッスンをカートに追加しました！" 重複: "⚠️
	 *         このレッスンはすでにカートに追加されています。" 未ログイン: "refuse"
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
	 * カート一覧画面表示メソッド セッションに保存されているカート内容を取得し表示 ログイン必須機能：未ログインの場合はログイン画面にリダイレクト
	 * 
	 * URL: GET /lesson/show/cart
	 * 
	 * @param session HTTPセッション（ログイン情報・カート情報取得用）
	 * @param model   Spring MVCのModelオブジェクト（画面へのデータ受け渡し用）
	 * @return String 遷移先画面のテンプレートファイル名 ログイン済み: user_planned_application.html 未ログイン:
	 *         /user/login.html
	 */
	@GetMapping("/lesson/show/cart")
	public String getLessonShowCart(HttpSession session, Model model) {
		// ログインチェック
		User loginUser = (User) session.getAttribute("loginUserInfo");
		// 未ログインならログイン画面へ
		if (loginUser == null) {


			return "redirect:/user/login";
		}
		// ログイン済みの場合
		List<Lesson> list = (List<Lesson>) session.getAttribute("list");
		if (list == null) {
			list = new ArrayList<>();
		}
		model.addAttribute("loginFlg", true);
		model.addAttribute("list", list);
		return "user_planned_application.html";
	}

	// カート商品削除機能
	@GetMapping("/lesson/cart/delete/{lessonId}")
	public String lessonCartDelete(@PathVariable Long lessonId, HttpSession session, Model model) {
		List<Lesson> list = (List<Lesson>) session.getAttribute("list");
		if (list != null) {
			list.removeIf(lesson -> lesson.getLessonId().equals(lessonId));
			session.setAttribute("list", list); // 更新
		}
		return "redirect:/lesson/show/cart";
	}

	
	/**
	 * 支払い方法選択画面を表示するメソッド（GET）
	 * ログインユーザーかどうかを確認し、未ログインならログイン画面にリダイレクト。
	 * セッション内の講座リストが存在しない場合はメニュー画面に戻る。
	 */

	@GetMapping("/lesson/request")
	public String applySelectPayment(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loginUserInfo");
		if (user == null) {
			return "redirect:/user/login";
		}
		List<Lesson> list = (List<Lesson>) session.getAttribute("list");
		if (list == null || list.size() == 0) {
			return "redirect:/lesson/menu";
		} else {
			model.addAttribute("list", list);
		}
		model.addAttribute("loginFlg", true);
		model.addAttribute("payFlg", false);

		return "user_apply_select_payment.html";
	}

	@PostMapping("/lesson/confirm")
	public String confirmApplyDetail(@RequestParam("payment") Integer payment, HttpSession session, Model model) {
		User user = (User) session.getAttribute("loginUserInfo");
		if (user == null) {
			return "redirect:/user/login";
		}
		List<Lesson> list = (List<Lesson>) session.getAttribute("list");
		if (list == null || list.size() == 0) {
			return "redirect:/lesson/menu";
		} else {
			model.addAttribute("list", list);
		}

		String payMethod = "";
		switch (payment) {
		case 0:
			payMethod = "当日現金支払い（無料講座の場合は、こちらを選択する。）";
			break;
		case 1:
			payMethod = "事前銀行振込";
			break;
		case 2:
			payMethod = "クレジットカード決済";
			break;
		}
		model.addAttribute("payMethod", payMethod);
		model.addAttribute("loginFlg", true);
		model.addAttribute("payFlg", true);

		int amount = 0;
		for (Lesson lesson : list) {
			amount += lesson.getLessonFee();
		}
		session.setAttribute("amount", amount);
		model.addAttribute("amount", amount);

		return "user_confirm_apply_detail.html";
	}

	
	/**
	 * 支払い方法を選択し、確認画面へ遷移するメソッド（POST）
	 * 選択された支払い方法に応じて説明を表示。
	 * また、講座の合計金額を計算し、セッションに保存。
	 */

	@PostMapping("/lesson/pay")
	public String applyComplete(@RequestParam("stripeEmail") String stripeEmail, HttpSession session, Model model) {
		User user = (User) session.getAttribute("loginUserInfo");
		if (user == null) {
			return "redirect:/user/login";
		}

		model.addAttribute("loginFlg", true);
		System.out.println("stripeEmail: " + stripeEmail);

		int amount = (int) session.getAttribute("amount");
		TransactionHistory transactionHistory = new TransactionHistory(user.getUserId(), amount,
				new Timestamp(System.currentTimeMillis()));
		transactionHistory = transactionHistoryDao.save(transactionHistory);

		List<Lesson> list = (List<Lesson>) session.getAttribute("list");
		for (Lesson lesson : list) {
			transactionItemDao.save(new TransactionItem(lesson.getLessonId(), transactionHistory.getTransactionId()));
		}

		session.removeAttribute("list");
		return "user_apply_complete.html";
	}

	/**

	 * 支払い処理完了後の処理メソッド（POST）
	 * Stripe支払い情報を受け取り、取引履歴とアイテムをDBに保存。
	 * カート情報をセッションから削除し、完了画面を表示。
	 */
	@GetMapping("/lesson/menu/search")
	public String getMenuSearch(@RequestParam("keyword") String keyword,
								Model model) {
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
		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();
		List<Lesson> lessonList = lessonDao.findByLessonNameContainingAndDateTimeCondition(keyword, localDate, localTime);
		model.addAttribute("lessonList", lessonList);

		return "user_menu.html";
}
   /** マイページ画面表示メソッド
	 * DBに保存されている購入済み講座内容を取得し表示
	 * ログイン必須機能：未ログインの場合はログイン画面にリダイレクト

	 * 
	 * URL: GET /lesson/mypage
	 * 
	 * セッションからuserIdを取得して紐づいた購入履歴を表示
	 * 
	 * @param session HTTPセッション（ログイン情報・カート情報取得用）
	 * @param model   Spring MVCのModelオブジェクト（画面へのデータ受け渡し用）
	 * @return String 遷移先画面のテンプレートファイル名 ログイン済み: mypage.html 未ログイン: /user/login.html
	 */
	@GetMapping("/lesson/mypage")
	public String getMYpage(HttpSession session, Model model) {
		// ログインチェック
		User loginUser = (User) session.getAttribute("loginUserInfo");
		// 未ログインならログイン画面へ
		if (loginUser == null) {
			return "user_login.html";
		}
		// ログイン済みの場合
		model.addAttribute("loginName", loginUser.getUserName());
		model.addAttribute("loginFlg", true);
		// userIdをセッションから取得する
		Long userId = loginUser.getUserId();
		// 紐づいた購入講座情報を検索して情報を渡す
		List<LessonWithTransactionDto> listSub = lessonService.getLessonPurchases(userId);
		model.addAttribute("listSub", listSub);
		return "mypage.html";
	}

	// 購入履歴削除機能
//	@PostMapping("/lesson/history/delete")
//	public String lessonHistoryDelete(@PathVariable Long lessonId, HttpSession session, Model model) {
//		// ログインチェック
//		User loginUser = (User) session.getAttribute("loginUserInfo");
//		// 未ログインならログイン画面へ
//		if (loginUser == null) {
//			return "user_login.html";
//		}
//		// ログイン済みの場合
//		model.addAttribute("loginName", loginUser.getUserName());
//		model.addAttribute("loginFlg", true);
//		// sessionから
//		/**
//		 * 削除機能作成中
//		 * 
//		 * 
//		 * 
//		 */
//		return "redirect:/lesson/mypage";
//	}
}