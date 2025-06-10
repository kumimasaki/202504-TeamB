package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.services.UserService;

@Controller
public class UserConfirmRegisterController {

	@Autowired
	private UserService userService;

	// 画面の表示
	@GetMapping("/user/confirm")
	public String getUserConfirmRegister() {
		return "user_confirm_register.html";
	}

	/**
	 * ユーザー登録処理メソッド
	 * 
	 * URL: POST /user/register/process 機能: ユーザー登録フォームから送信されたデータを処理する
	 * 
	 * @param userName     ユーザー名（HTMLフォームから送信）
	 * @param userEmail    メールアドレス（HTMLフォームから送信）
	 * @param userPassword パスワード（HTMLフォームから送信）
	 * @return String 遷移先ページのファイル名 成功時：user_login.html（ログイン画面に遷移）
	 *         失敗時：user_register.html（登録画面に留まる）
	 */

	// 登録処理
	// ユーザーサービスのcreateUserメソッドを呼び出して登録処理を実行
	// createUserがtrueを返す場合：登録成功、user_login.htmlに遷移
	// createUserがfalseを返す場合：登録失敗（メール重複等）、user_register.htmlに留まる
	// 登録成功：確認画面に遷移
	@PostMapping("/user/register/process")
	public String userRegisterProcess(@RequestParam String userName, @RequestParam String userEmail,
			@RequestParam String userPassword) {

		// ユーザーサービスのcreateUserメソッドを呼び出して登録処理を実行
		// createUserがtrueを返す場合：登録成功、user_login.htmlに遷移
		// createUserがfalseを返す場合：登録失敗（メール重複等）、user_register.htmlに留まる
		if (userService.createUser(userEmail, userName, userPassword)) {
			// 登録成功：ログイン画面に遷移
			return "user_login.html";
		} else {
			// 登録失敗：登録画面に留まる（エラー表示も可能）
			return "user_register.html";
		}
	}

}
