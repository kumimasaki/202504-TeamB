package ec.com.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ec.com.model.dao.UserDao;
import ec.com.model.entity.User;
import ec.com.services.UserService;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	private UserDao userDao;

	@Autowired
	private UserService userService;

	private User testUser;


	@BeforeEach
	public void prepareDate() {
		// ユーザーオブジェクトの作成
		testUser = new User();
		testUser.setUserId(1L);
		testUser.setUserEmail("Alice@test");
		testUser.setUserPassword("1234");
		testUser.setUserName("テストユーザー");

		// ログイン成功 : userEmailがAliceの場合、 ユーザーオブジェクトの内容を返す
		when(userDao.findByUserEmailAndUserPassword("Alice@test", "1234")).thenReturn(testUser);
		// ログイン失敗 ： userEmailがAnaの場合、nullを返す
		when(userDao.findByUserEmailAndUserPassword("Ana@test", "1234")).thenReturn(null);
	}

	// login成功時
	@Test
	public void testLoginUser_success() {

		User result = userService.loginUser("Alice@test", "1234");
		assertNotNull(result);
		assertEquals("Alice@test", result.getUserEmail());
	}

	// login失敗時
	// メールアドレスが不正
	@Test
	public void testLoginUser_userEmailfailure() {
		User result = userService.loginUser("Ana@test", "1234");
		assertNull(result);
	}
	
	// パスワードが不正
	@Test
	public void testLoginUser_passwordfailure() {
		User result = userService.loginUser("Alice@test", "1111");
		assertNull(result);
	}
	
	//メールアドレスがnull
	@Test
	public void testLoginUser_nullEmail() {
		User result = userService.loginUser("", "1234");
		assertNull(result);
	}
	
	//パスワードがnull
	@Test
	public void testLoginUser_nullPassword() {
		User result = userService.loginUser("Alice@test", "");
		assertNull(result);
	}
}
