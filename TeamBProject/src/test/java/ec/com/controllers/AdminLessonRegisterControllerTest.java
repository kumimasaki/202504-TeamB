package ec.com.controllers;

import ec.com.model.entity.Lesson;
import ec.com.services.LessonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminLessonRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    // 登録画面表示（ログインあり）
    @Test
    public void testShowRegisterFormWithLogin() throws Exception {
        mockMvc.perform(get("/admin/lesson/register")
                        .sessionAttr("AdminLogin", new ec.com.model.entity.Admin()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_register_lesson.html"));
    }

    // 登録画面表示（ログインなし）
    @Test
    public void testShowRegisterFormWithoutLogin() throws Exception {
        mockMvc.perform(get("/admin/lesson/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    // 講座登録 正常系
    @Test
    public void testRegisterLessonSuccess() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageName", "test.jpg", "image/jpeg", "DummyImageData".getBytes());

        Mockito.doNothing().when(lessonService).registerLesson(any(Lesson.class));

        mockMvc.perform(multipart("/admin/lesson/register")
                        .file(imageFile)
                        .param("startDate", "2025-06-30")
                        .param("startTime", "10:00")
                        .param("finishTime", "16:00")
                        .param("lessonName", "講座テスト")
                        .param("lessonDetail", "テストです")
                        .param("lessonFee", "10000")
                        .param("capacity", "2")
                        .sessionAttr("AdminLogin", new ec.com.model.entity.Admin()))
                .andExpect(status().isOk()) 
                .andExpect(view().name("admin_fix_register"));
    }


    // 講座登録 異常系 - 必須項目不足（リダイレクトではなく再表示を期待）
    @Test
    public void testRegisterLessonMissingFields() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageName", "", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/admin/lesson/register")
                        .file(imageFile)
                        .param("startDate", "")
                        .param("startTime", "")
                        .param("finishTime", "")
                        .param("lessonName", "")
                        .param("lessonDetail", "")
                        .param("lessonFee", "")
                        .param("capacity", "")
                        .sessionAttr("AdminLogin", new ec.com.model.entity.Admin()))
                .andExpect(status().isOk())  //
                .andExpect(view().name("admin_register_lesson.html"))
                .andExpect(model().attributeExists("registerError"));
    }


}
