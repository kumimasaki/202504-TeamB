package ec.com.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import ec.com.model.entity.User;
import ec.com.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @BeforeEach
	public void prepareData() {
        User mockUser = new User();
        mockUser.setUserEmail("ake@test.com");
        mockUser.setUserPassword("1234abcd");
        Mockito.when(userService.loginUser("ake@test.com", "1234abcd")).thenReturn(mockUser);
        
        Mockito.when(userService.loginUser("test@test.com", "1234abcd")).thenReturn(null);

    }

    // ログイン画面の表示テスト（GET）
    @Test
    public void testLoginPageDisplay() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_login.html"));
    }

    // 正しいメールアドレスとパスワードでログイン成功
    @Test
    public void testLoginSuccess() throws Exception {
        // Mockセッション作成
        MockHttpSession session = new MockHttpSession();

        MvcResult result = mockMvc.perform(post("/user/login/process")
                        .param("userEmail", "ake@test.com")
                        .param("userPassword", "1234abcd")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lesson/menu"))
                .andReturn();

        // セッションにユーザー情報が格納されたか検証
        User sessionUser = (User) session.getAttribute("loginUserInfo");
        assertThat(sessionUser).isNotNull();
        assertThat(sessionUser.getUserEmail()).isEqualTo("ake@test.com");
    }

    // 間違ったログイン情報でログイン失敗
    @Test
    public void testLoginFailure() throws Exception {
    	// 認証失敗を模擬
        mockMvc.perform(post("/user/login/process")
                        .param("userEmail", "test@test.com")
                        .param("userPassword", "1234abcd")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("user_login.html"))
                .andExpect(model().attributeExists("loginError"));
    }
}
