package ec.com.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // No.1 表示テスト：ログイン画面が表示されること
    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin_login"));
    }

    // No.2 正しいログイン情報を送信する
    @Test
    public void testLoginWithCorrectCredentials() throws Exception {
        mockMvc.perform(post("/admin/login")
        		.param("email", "ake@test.com")
                .param("password", "1234abcd"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/lesson/all"));
    }

    // No.3 不存在メールアドレス
    @Test
    public void testLoginWithNonexistentEmail() throws Exception {
        mockMvc.perform(post("/admin/login")
                .param("email", "none@test.com")
                .param("password", "1234abcd"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?error=true"));
    }

    // No.4 正しいメール・誤ったパスワード
    @Test
    public void testLoginWithWrongPassword() throws Exception {
        mockMvc.perform(post("/admin/login")
                .param("email", "admin@test.com")
                .param("password", "wrongpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?error=true"));
    }

    // No.5 両方間違い
    @Test
    public void testLoginWithWrongEmailAndPassword() throws Exception {
        mockMvc.perform(post("/admin/login")
                .param("email", "wrong@test.com")
                .param("password", "wrongpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?error=true"));
    }

    // No.6 logoutテスト
    @Test
    public void testLogoutRedirect() throws Exception {
        mockMvc.perform(get("/admin/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }
}
