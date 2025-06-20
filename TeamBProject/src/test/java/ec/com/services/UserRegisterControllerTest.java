package ec.com.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Timestamp;

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
    private UserService userService;

    @MockBean
    private UserDao userDao;
    
    @BeforeEach
	public void prepareData() {
    	Mockito.when(userDao.findByUserEmail("already@test.com")).thenReturn(new User());
    	Mockito.when(userDao.findByUserEmail("aaa@test.com")).thenReturn(null);
    	Mockito.when(userDao.findByUserEmail("error@test.com")).thenThrow(new RuntimeException("DBエラー"));
    }

    // 正常系テスト：メールアドレスが未登録で登録成功
    @Test
    public void testCreateUser_Success() {
        boolean result = userService.createUser("aaa@test.com", "テスト太郎", "abcd1234");
        assertTrue(result);
    }

    // 異常系テスト：既にメールアドレスが登録されている場合
    @Test
    public void testCreateUser_EmailAlreadyExists() {
        boolean result = userService.createUser("already@test.com", "テスト花子", "abcd1234");
        assertFalse(result);
    }
    
    // 模拟在保存时发生异常
    @Test
    public void testCreateUser_SaveThrowsException() {
        boolean result = userService.createUser("error@test.com", "エラーユーザー", "abcd1234");
        assertFalse(result);
    }

}
