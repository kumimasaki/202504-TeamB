package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ec.com.model.dao.AdminDao;
import ec.com.model.entity.Admin;
import ec.com.services.AdminService;

/**
 * 管理者登録関連の処理を提供するコントローラ
 * <ul>
 *   <li>登録画面表示</li>
 *   <li>登録内容確認</li>
 *   <li>管理者登録実行</li>
 *   <li>メールアドレス重複チェック（AJAX用）</li>
 * </ul>
 */
@Controller
public class AdminRegisterController {

    @Autowired
    // 管理者登録処理を担うサービス
    private AdminService adminService; 

    @Autowired 
    // 管理者データ取得用DAO（重複チェックなど）
    private AdminDao adminDao;         
    

    /**
     * 管理者登録フォームを表示する
     * @return 登録フォームのテンプレート名
     */
    @GetMapping("/admin/register")
    public String getAdminRegister() {
        // 登録画面を返却
        return "admin_register.html";
    }

    /**
     * 登録内容確認画面へ遷移する
     * <ol>
     *   <li>パスワードと確認用パスワードの一致チェック</li>
     *   <li>不一致時はエラーメッセージをモデルに設定し、登録画面に戻す</li>
     *   <li>一致時は入力値をモデルにセットし、確認画面を表示</li>
     * </ol>
     *
     * @param adminName 管理者名
     * @param adminEmail メールアドレス
     * @param password パスワード
     * @param adminPassword パスワード再確認用
     * @param model ビューに渡すモデル
     * @return 確認画面または登録画面へのテンプレート名
     */
    @PostMapping("/admin/confirm")
    public String adminRegisterConfirm(
            @RequestParam String adminName,
            @RequestParam String adminEmail,
            @RequestParam String password,
            @RequestParam String adminPassword,
            Model model) {

        // パスワード一致チェック
        if (!password.equals(adminPassword)) {
            model.addAttribute("error", "パスワードが一致しません。");
            // 不一致の場合、再度登録画面を表示
            return "admin_register.html"; 
        }

        // 確認画面用に入力値をモデルにセット
        model.addAttribute("adminName", adminName);
        model.addAttribute("adminEmail", adminEmail);
        model.addAttribute("adminPassword", adminPassword);
        // 確認画面を表示
        return "admin_confirm_register.html"; 
    }

    /**
     * 実際に管理者情報を登録する
     * <ol>
     *   <li>AdminServiceのcreateAdminを呼び出し</li>
     *   <li>登録成功時はログイン画面へリダイレクト</li>
     *   <li>登録失敗時は登録画面へリダイレクト</li>
     * </ol>
     *
     * @param adminName 登録する管理者名
     * @param adminEmail 登録するメールアドレス
     * @param adminPassword 登録するパスワード
     * @param model ビューに渡すモデル（使用しない）
     * @return リダイレクト先URL
     */
    @PostMapping("/admin/register/process")
    public String adminRegisterProcess(
            @RequestParam String adminName,
            @RequestParam String adminEmail,
            @RequestParam String adminPassword,
            Model model) {

        // 管理者登録処理を実行
        boolean success = adminService.createAdmin(adminName, adminEmail, adminPassword);
        if (success) {
            // 登録成功: ログイン画面へ遷移
            return "redirect:/admin/login";
        } else {
            // 登録失敗: 登録画面に戻る
            return "redirect:/admin/register";
        }
    }

    /**
     * メールアドレス重複チェック（AJAX用）
     * @param email 確認したいメールアドレス
     * @return 未登録ならtrue、既に登録済みならfalseを返却
     */
    @GetMapping("/admin/check")
    @ResponseBody
    public boolean checkAdminEmail(@RequestParam String email) {
        // DAOで管理者を検索
        Admin admin = adminDao.findByAdminEmail(email);
        // 存在しなければtrue、存在すればfalse
        return (admin == null);
    }

}
