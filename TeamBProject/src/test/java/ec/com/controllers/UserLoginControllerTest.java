package ec.com.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import ec.com.model.entity.User;
import ec.com.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private User testUser;

	@BeforeEach
	public void prepareDate() {

		testUser = new User();
		testUser.setUserEmail("Alice@test");
		testUser.setUserPassword("1234");
		testUser.setUserName("テストユーザー");

		// login成功 userEmail "Alice@test"、 password "1234" true
		when(userService.loginUser("Alice@test", "1234")).thenReturn(testUser);

		// login失敗： userEmail "Bob@test"と等しい、 パスワードはどんな値でもいい false
		when(userService.loginUser(eq("Bob@test"), any())).thenReturn(null);
	}

	// ログインページを取得するテスト
	@Test
	public void testGetUserLoginPage_succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/user/login");

		mockMvc.perform(request).andExpect(view().name("user_login.html"));
	}

	// @loginが成功したときのテスト
	@Test
	public void testLogin_success() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
				.param("userEmail", "Alice@test")
				.param("userPassword", "1234");
		// ログインが成功したら「/lesson/menu」にリダイレクトして、入力された値が渡されているかのテスト
		mockMvc.perform(request).andExpect(request().sessionAttribute("loginUserInfo", testUser))
				.andExpect(redirectedUrl("/lesson/menu"));
	}

	// loginが失敗したときのテスト
	@Test
	public void testLogin_failed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
				.param("userEmail", "Bob@test")
				.param("userPassword", "1234");
		// ログインが失敗したら「user_login.html」に遷移して、入力された値が渡されているかのテスト
		mockMvc.perform(request).andExpect(model().attribute("loginError", "メールアドレスまたはパスワードが違います"))
				.andExpect(view().name("user_login.html"));
	}

}
