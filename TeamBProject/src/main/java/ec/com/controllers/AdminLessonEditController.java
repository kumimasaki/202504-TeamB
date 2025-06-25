package ec.com.controllers;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AdminLessonEditController {

	@Autowired
	// 講座の取得・更新処理を担うサービスをDI
	private LessonService lessonService;

	@Autowired
	// セッション管理用オブジェクトをDI
	private HttpSession session;

	/**
	 * 編集画面表示 指定されたlessonIdで講座情報を取得し、編集フォームにバインドして表示します。
	 *
	 * @param lessonId 編集対象の講座ID
	 * @param model    ビューに渡すモデル
	 * @return 編集画面のテンプレート名、または該当講座がない場合は一覧画面へリダイレクト
	 */
	@GetMapping("/admin/lesson/edit/{lessonId}")
	public String showEditForm(@PathVariable Long lessonId, Model model) {
		// DBからlessonIdに対応する講座を取得
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			// 該当講座が存在しない場合一覧画面へリダイレクト
			return "redirect:/admin/lesson/all";
		}
		// ビューに講座情報を渡す
		model.addAttribute("lesson", lesson);
		return "admin_edit_lesson";
	}

	/**
	 * 編集内容保存 フォームから送信された講座情報を検証し、問題なければ更新処理を実行します。
	 *
	 * @param lesson   フォームの入力値をバインドしたLessonオブジェクト
	 * @param lessonId フォームから送られてくる講座ID
	 * @param model    ビューに渡すモデル（エラー時に再表示するため）
	 * @return 更新成功時は完了画面、エラー時は編集画面を再表示
	 */
	@PostMapping("/admin/lesson/edit/update")
	public String updateLesson(@ModelAttribute Lesson lesson, @RequestParam Long lessonId, Model model) {

		// --- 日時バリデーション用に各値を取得 ---
		// 開始日
		LocalDate startDate = lesson.getStartDate();
		// 開始時間
		LocalTime startTime = lesson.getStartTime();
		// 終了時間
		LocalTime finishTime = lesson.getFinishTime();
		// 現在日時
		LocalDateTime now = LocalDateTime.now();

		// 全ての日時情報が揃っている場合にのみチェック実行
		if (startDate != null && startTime != null && finishTime != null) {
			// ① 開始日時が現在より前になっていないか
			LocalDateTime lessonStartDateTime = LocalDateTime.of(startDate, startTime);
			if (lessonStartDateTime.isBefore(now)) {
				model.addAttribute("editError", "開始日時は現在より後にしてください。");
				model.addAttribute("lesson", lesson);
				// エラー時は再編集画面へ
				return "admin_edit_lesson";

			}
			// ② 開始日が今日より前になっていないか
			if (startDate.isBefore(now.toLocalDate())) {
				model.addAttribute("editError", "開始日は今日以降にしてください。");
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson";
			}
			// ③ 開始時間が終了時間より遅くないか
			if (startTime.isAfter(finishTime)) {
				model.addAttribute("editError", "開始時間は終了時間より前にしてください。");
				model.addAttribute("lesson", lesson);
				return "admin_edit_lesson";
			}
		}

		// --- バリデーションをパスしたら更新処理を実行 ---
		lessonService.updateLesson(lesson);

		// 更新完了後に完了画面へ遷移。lessonIdを渡して再編集リンク等を表示できるように
		model.addAttribute("lessonId", lesson.getLessonId());
		return "admin_fix_edit";
	}

	/**
	 * 画像変更ページ表示 画像のみ別画面で編集するため、対象講座情報を取得してフォームに渡します。
	 *
	 * @param lessonId 編集対象の講座ID
	 * @param model    ビューに渡すモデル
	 * @return 画像編集画面のテンプレート名、または該当講座がない場合は一覧画面へリダイレクト
	 */
	@GetMapping("/admin/lesson/image/edit/{lessonId}")
	public String getAdminLessonImageEditPage(@PathVariable Long lessonId, Model model) {
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}
		// 画像編集用にlessonオブジェクトをモデルにセット
		model.addAttribute("lesson", lesson);
		return "admin_edit_lesson_img";
	}

	/**
	 * 画像アップロード＆講座更新処理 ファイル保存とその他項目の更新を一度に行い、一覧画面へリダイレクトします。
	 *
	 * @param imageFile    アップロードされたMultipartFile（画像）
	 * @param lessonId     更新対象の講座ID
	 * @param startDate    文字列形式の開始日（yyyy-MM-dd）
	 * @param startTime    文字列形式の開始時間（HH:mm:ss）
	 * @param finishTime   文字列形式の終了時間（HH:mm:ss）
	 * @param lessonName   講座名
	 * @param lessonDetail 講座詳細
	 * @param lessonFee    講座料金（文字列）
	 * @return 一覧画面へのリダイレクトURL（エラー時は同画面にerrorパラメータ付き）
	 */
	@PostMapping("/admin/lesson/image/edit/update")
	public String updateLessonImage(@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam("lessonId") Long lessonId, @RequestParam("startDate") String startDate,
			@RequestParam("startTime") String startTime, @RequestParam("finishTime") String finishTime,
			@RequestParam("lessonName") String lessonName, @RequestParam("lessonDetail") String lessonDetail,
			@RequestParam("lessonFee") String lessonFee) {

		// DBから対象の講座情報を取得
		Lesson lesson = lessonService.findById(lessonId);
		if (lesson == null) {
			return "redirect:/admin/lesson/all";
		}

		// --- 画像ファイルが存在する場合のみ保存処理 ---
		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				// 元のファイル名取得
				String originalFileName = imageFile.getOriginalFilename();
				// タイムスタンプ付きの新ファイル名生成
				String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
				String newFileName = timestamp + "_" + originalFileName;
				// 保存先パスを指定（static配下）
				Path savePath = Paths.get("src/main/resources/static/lesson-image/" + newFileName);
				// ファイルをコピー（上書き可）
				Files.copy(imageFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
				// エンティティにファイル名をセット
				lesson.setImageName(newFileName);
			} catch (IOException e) {
				e.printStackTrace();
				// 保存失敗時はエラーをクエリパラメータで渡してリダイレクト
				return "redirect:/admin/lesson/image/edit/" + lessonId + "?error=true";
			}
		}

		// --- その他フォーム項目の値を更新 ---
		if (startDate != null && !startDate.isBlank()) {
			lesson.setStartDate(LocalDate.parse(startDate));
		}
		if (startTime != null && !startTime.isBlank()) {
			lesson.setStartTime(LocalTime.parse(startTime));
		}
		if (finishTime != null && !finishTime.isBlank()) {
			lesson.setFinishTime(LocalTime.parse(finishTime));
		}
		lesson.setLessonName(lessonName);
		lesson.setLessonDetail(lessonDetail);
		if (lessonFee != null && !lessonFee.isBlank()) {
			lesson.setLessonFee(Integer.parseInt(lessonFee));
		}

		// --- 講座情報をDBに更新 ---
		lessonService.updateLesson(lesson);

		// 更新後は一覧画面へリダイレクト
		return "redirect:/admin/lesson/edit/" + lessonId;
	}

}
