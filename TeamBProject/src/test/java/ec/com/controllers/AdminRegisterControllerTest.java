package ec.com.controllers;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ec.com.model.dao.AdminDao;
import ec.com.model.entity.Admin;
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
		// emailが存在すれば、対象を返す
		Admin existingAdmin = new Admin();
		existingAdmin.setAdminEmail("test@example.com");
		when(adminDao.findByAdminEmail("test@example.com")).thenReturn(existingAdmin);
		// emailが存在しない、nullを返す
		when(adminDao.findByAdminEmail("New@test.com")).thenReturn(null);

	}

	// "/admin/register" へのGETリクエストを作成し、実行する。
	// 1;表示テスト
	@Test
	public void testGetAdminRegisterPage() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/admin/register");
		mockMvc.perform(request).andExpect(view().name("admin_register.html"));

	}

	// 2: 登録処理＿成功 @PostMapping("/account/register/process")
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

	// 3: 登録処理＿失敗
	@Test
	public void testAdminRegisterFail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/register/process").param("adminName", "Taro")
				.param("adminEmail", "test@test.com").param("adminPassword", "1234abcd"))
				.andExpect(view().name("redirect:/admin/register"));

		verify(adminService, times(1)).createAdmin("Taro", "test@test.com", "1234abcd");
	}

	// 4:確認画面_異常テスト_パスワード不一致

	@Test
	public void testAdminConfirm_PasswordMismatch() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/confirm").param("adminName", "Akemi")
				.param("adminEmail", "ake@test.com").param("password", "1234abcd").param("adminPassword", "abcd1234"))
				.andExpect(model().attribute("error", "パスワードが一致しません。")).andExpect(view().name("admin_register.html"));
	}

	// 5:確認画面_成功
	@Test
	public void testAdminConfirm_Success() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/confirm").param("adminName", "Akemi")
				.param("adminEmail", "ake@test.com").param("password", "1234abcd").param("adminPassword", "1234abcd"))
				.andExpect(model().attribute("adminName", "Akemi"))
				.andExpect(model().attribute("adminEmail", "ake@test.com"))
				.andExpect(model().attribute("adminPassword", "1234abcd"))
				.andExpect(view().name("admin_confirm_register.html"));
	}

	// 6:email==null true
	@Test
	public void testCheckAdminEmail_NotExists() throws Exception {
		mockMvc.perform(get("/admin/check").param("email", "New@test.com")).andExpect(status().isOk())
				.andExpect(content().string("true"));
	}

	// 7:email false
	@Test
	public void testCheckAdminEmail_Exists() throws Exception {
		mockMvc.perform(get("/admin/check").param("email", "test@example.com")).andExpect(status().isOk())
				.andExpect(content().string("false"));
	}

}
