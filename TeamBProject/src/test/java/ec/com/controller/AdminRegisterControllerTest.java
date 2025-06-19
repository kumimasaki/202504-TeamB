package ec.com.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import ec.com.model.dao.AdminDao;
import ec.com.services.AdminService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AdminService adminService;

	@MockBean
	private AdminDao adminDao;

	// サービスクラスを使ったデータ作成
	@BeforeEach
	public void prepareData() {
		// 登録できる場合 name(Akemi)、mail(ake@test.com)とpassword(1234abcd)を入力
		when(adminService.createAdmin("Akemi", "ake@test.com", "1234abcd")).thenReturn(true);
		// ログインが失敗 name（Taro）、すでに登録されているmail(test@test.com)とpassword(1234)を入力
		when(adminService.createAdmin("Taro", "test@test.com", "1234abcd")).thenReturn(false);
	}

	// "/admin/register" へのGETリクエストを作成し、実行する。
	@Test
	public void testGetAdminRegisterPage() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/admin/register");
		mockMvc.perform(request).andExpect(view().name("admin_register.html"));

	}

	// 登録処理 @PostMapping("/account/register/process")
	/*
	 * 登録が成功するかのテスト ユーザー名(LRfei)、メールアドレス(test@test.com)及びパスワード(1234)を入力
	 * /account/register/process" へのPOSTリクエストを作成し、実行する
	 */
	@Test
	public void testAdminRegister_NewAdmin_Succeed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/register/process").param("adminName", "Akemi")
				.param("adminEmail", "ake@test.com").param("adminPassword", "1234abcd"))
				.andExpect(view().name("redirect:/admin/login"));
		verify(adminService, times(1)).createAdmin("Akemi", "ake@test.com", "1234abcd");
	}

	@Test
	public void testAdminRegisterFail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/register/process").param("adminName", "Taro")
				.param("adminEmail", "test@test.com").param("adminPassword", "1234abcd"))
				.andExpect(view().name("redirect:/admin/register"));

		// 验证 createAdmin 被正确调用一次
		verify(adminService, times(1)).createAdmin("Taro", "test@test.com", "1234abcd");
	}
}
