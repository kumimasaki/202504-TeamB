package ec.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ec.com.model.entity.Admin;
import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;

/**
 * 管理者用：新規講座登録機能を提供するコントローラ
 * <ul>
 *   <li>講座登録フォームの表示</li>
 *   <li>フォーム入力値のバリデーション</li>
 *   <li>画像ファイルの保存</li>
 *   <li>講座情報のDB登録</li>
 * </ul>
 */
@Controller
public class AdminLessonRegisterController {

    @Autowired
    private LessonService lessonService;  // 講座登録などのビジネスロジックを扱うサービス

    /**
     * 講座登録フォーム表示
     * <ol>
     *   <li>セッションからAdminログイン情報を取得</li>
     *   <li>未ログインの場合、ログイン画面へリダイレクト</li>
     *   <li>ログイン済みの場合、登録フォームを表示</li>
     * </ol>
     *
     * @param session HTTPセッション（AdminLogin属性を確認）
     * @return 登録フォームのテンプレート名
     */
    @GetMapping("/admin/lesson/register")
    public String showRegisterForm(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("AdminLogin");
        if (admin == null) {
            // 未ログインの場合はログイン画面へ
            return "redirect:/admin/login";
        }
        // 登録フォーム表示
        return "admin_register_lesson.html";
    }

    /**
     * 新規講座登録処理（DTOを使用しないシンプル版）
     * <ol>
     *   <li>リクエストパラメータのバリデーション</li>
     *   <li>日付・時間の妥当性チェック</li>
     *   <li>画像ファイルの保存（タイムスタンプ付きリネーム）</li>
     *   <li>Lessonエンティティへの値セット</li>
     *   <li>Service経由でDB登録</li>
     *   <li>登録完了画面へ遷移</li>
     * </ol>
     *
     * @param imageFile    アップロード画像ファイル
     * @param startDate    開始日（"yyyy-MM-dd"形式）
     * @param startTime    開始時間（"HH:mm:ss"形式）
     * @param finishTime   終了時間（"HH:mm:ss"形式）
     * @param lessonName   講座名
     * @param lessonDetail 講座詳細
     * @param lessonFee    料金（文字列）
     * @param capacity     定員（文字列）
     * @param session      HTTPセッション（AdminLogin確認）
     * @param model        エラー時にメッセージを表示するためのモデル
     * @return 登録完了画面またはエラー時は登録フォーム
     */
    @PostMapping("/admin/lesson/register")
    public String registerLesson(
            @RequestParam("imageName") MultipartFile imageFile,
            @RequestParam("startDate") String startDate,
            @RequestParam("startTime") String startTime,
            @RequestParam("finishTime") String finishTime,
            @RequestParam("lessonName") String lessonName,
            @RequestParam("lessonDetail") String lessonDetail,
            @RequestParam("lessonFee") String lessonFee,
            @RequestParam("capacity") String capacity,
            HttpSession session,
            Model model) {

        // --- 入力必須チェック ---
        if (lessonName.isBlank() || lessonDetail.isBlank() || startDate.isBlank()
                || startTime.isBlank() || finishTime.isBlank()
                || lessonFee.isBlank() || capacity.isBlank()) {
            model.addAttribute("registerError", "すべての項目を入力してください。");
            return "admin_register_lesson.html";
        }

        // --- 日付・時間のパース ---
        LocalDate startDateParsed   = LocalDate.parse(startDate);
        LocalTime startTimeParsed   = LocalTime.parse(startTime);
        LocalTime finishTimeParsed  = LocalTime.parse(finishTime);
        LocalDate today             = LocalDate.now();

        // バリデーション①：過去日付禁止
        if (startDateParsed.isBefore(today)) {
            model.addAttribute("registerError", "開始日は今日以降の日付にしてください。");
            return "admin_register_lesson.html";
        }

        // バリデーション②：開始時間 > 終了時間禁止
        if (startTimeParsed.isAfter(finishTimeParsed)) {
            model.addAttribute("registerError", "開始時間は終了時間より前にしてください。");
            return "admin_register_lesson.html";
        }

        // バリデーション③：開始日時が現在より前禁止
        LocalDateTime now              = LocalDateTime.now();
        LocalDateTime lessonStartDateTime = LocalDateTime.of(startDateParsed, startTimeParsed);
        if (lessonStartDateTime.isBefore(now)) {
            model.addAttribute("registerError", "開始日時は現在より後にしてください。");
            return "admin_register_lesson.html";
        }

        // --- Lessonエンティティ生成 ---
        Lesson lesson = new Lesson();

        // --- 画像ファイル保存処理 ---
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String originalFileName = imageFile.getOriginalFilename();
                String timestamp        = LocalDateTime.now()
                                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String newFileName      = timestamp + "_" + originalFileName;
                Path savePath           = Paths.get(
                                              "src/main/resources/static/lesson-image/"
                                              + newFileName);
                Files.copy(imageFile.getInputStream(), savePath,
                           StandardCopyOption.REPLACE_EXISTING);
                lesson.setImageName(newFileName);  // 保存したファイル名を設定
            } catch (IOException e) {
                e.printStackTrace();
                // 保存失敗時はエラーパラメータ付きでリダイレクト
                return "redirect:/admin/lesson/register?error=true";
            }
        }

        // --- その他フォーム値をエンティティにセット ---
        lesson.setStartDate(startDateParsed);
        lesson.setStartTime(startTimeParsed);
        lesson.setFinishTime(finishTimeParsed);
        lesson.setLessonName(lessonName);
        lesson.setLessonDetail(lessonDetail);
        lesson.setLessonFee(Integer.parseInt(lessonFee));
        lesson.setCapacity(Integer.parseInt(capacity));
        // 登録日時をタイムスタンプとしてセット
        lesson.setRegisterDate(Timestamp.valueOf(LocalDateTime.now()));

        // --- DBへ登録実行 ---
        lessonService.registerLesson(lesson);

        // 登録完了画面へ遷移
        return "admin_fix_register";
    }
}
