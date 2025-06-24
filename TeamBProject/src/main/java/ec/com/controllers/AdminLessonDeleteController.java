package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;

/**
 * 管理者用：講座削除機能を提供するコントローラ
 * <p>
 * - 講座一覧から削除対象を選択する画面表示
 * - 削除要求を受けて講座を削除（購入履歴がある場合は削除不可）
 */
@Controller
public class AdminLessonDeleteController {

    @Autowired
 // 講座関連のビジネスロジックを扱うサービス
    private LessonService lessonService;  

    /**
     * 削除画面の初期表示
     * <ul>
     *   <li>全講座の売上・申込者数統計情報を取得</li>
     *   <li>モデルに登録し、削除選択画面をレンダリング</li>
     * </ul>
     * 
     * @param model ビューに渡すモデルオブジェクト
     * @return 講座削除画面のテンプレート名
     */
    @GetMapping("/admin/delete/lesson")
    public String getLessonDeletePage(Model model) {
        // サービスから全講座の統計情報を取得してモデルに追加
        model.addAttribute("statsList", lessonService.getLessonStatsList());
        // 削除画面を表示
        return "admin_delete_lesson";
    }

    /**
     * 講座削除処理
     * <ol>
     *   <li>指定IDの講座を検索</li>
     *   <li>存在しない場合、エラー表示して入力画面に戻る</li>
     *   <li>購入履歴がある場合、削除不可としてエラー表示</li>
     *   <li>問題なければ講座削除を実行し、完了画面を表示</li>
     * </ol>
     * 
     * @param lessonId 削除対象の講座ID
     * @param model    ビューに渡すモデルオブジェクト（エラー時に再利用）
     * @return 削除完了画面への遷移、またはエラー時は削除画面へ戻す
     */
    @PostMapping("/admin/lesson/delete/remove")
    public String lessonDelet(
            @RequestParam Long lessonId,
            Model model) {

        // 1. 講座の存在チェック：IDに該当する講座を取得
        Lesson lesson = lessonService.findById(lessonId);
        if (lesson == null) {
            // 指定IDの講座が存在しない場合、エラーメッセージを追加
            model.addAttribute("error", "指定された講座は存在しません。");
            // 再度統計情報を取得し画面を再表示
            model.addAttribute("statsList", lessonService.getLessonStatsList());
            return "admin_delete_lesson";
        }

        // 2. 購入履歴チェック：過去に購入があれば削除不可
        boolean hasTransaction = lessonService.hasTransaction(lessonId);
        if (hasTransaction) {
            // 購入履歴ありの場合のエラーメッセージ
            model.addAttribute("error", "この講座は既に購入されているため、削除できません。");
            // 再度統計情報を取得し画面を再表示
            model.addAttribute("statsList", lessonService.getLessonStatsList());
            return "admin_delete_lesson";
        }

        // 3. 削除実行：問題なければサービスに削除を依頼
        lessonService.deletByLesson(lessonId);

        // 4. 削除完了画面へ遷移（再編集リンクなどがあれば利用可能）
        return "admin_fix_delete";
    }

}

