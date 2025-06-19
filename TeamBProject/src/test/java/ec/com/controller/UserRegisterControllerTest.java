package ec.com.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import ec.com.model.dao.UserDao;
import ec.com.model.entity.User;
import ec.com.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
	public class UserRegisterControllerTest {

	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private UserService userService;

	    @MockBean
	    private UserDao userDao;

	    @BeforeEach
		public void prepareData() {
	    	Mockito.when(userDao.findByUserEmail("already@test.com")).thenReturn(new User());
	    	Mockito.when(userDao.findByUserEmail("aaa@test.com")).thenReturn(null);
	    }
	    // ユーザー登録画面表示
	    @Test
	    public void testGetUserRegisterPage() throws Exception {
	        mockMvc.perform(get("/user/register"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("user_register.html"));
	    }
	    
	    // パスワード一致 → 確認画面へ
	    @Test
	    public void testRegisterConfirmSuccess() throws Exception {
	        mockMvc.perform(post("/user/confirm/process")
	                .param("userName", "花子")
	                .param("userEmail", "aaa@test.com")
	                .param("userPassword", "abc12345")
	                .param("confirmPassword", "abc12345"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("user_confirm_register.html"))
	                .andExpect(model().attribute("userName", "花子"))
	                .andExpect(model().attribute("userEmail", "aaa@test.com"))
	                .andExpect(model().attribute("userPassword", "abc12345"));
	    }

	    // パスワード不一致 → 登録画面へ戻る
	    @Test
	    public void testRegisterConfirmFail() throws Exception {
	        mockMvc.perform(post("/user/confirm/process")
	                .param("userName", "花子")
	                .param("userEmail", "aaa@test.com")
	                .param("userPassword", "abc12345")
	                .param("confirmPassword", "abc123"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("user_Register.html"))
	                .andExpect(model().attributeExists("registerError"));
	    }

	    // 存在確認API (true: 可注册)
	    @Test
	    public void testCheckEmailAvailable() throws Exception {
	        mockMvc.perform(get("/user/check")
	                .param("email", "aaa@test.com"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("true"));
	    }

	    // 存在確認API (false: 已存在)
	    @Test
	    public void testCheckEmailExists() throws Exception {
	        mockMvc.perform(get("/user/check")
	                .param("email", "already@test.com"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("false"));
	    }
	}
