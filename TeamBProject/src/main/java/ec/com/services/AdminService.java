package ec.com.services;

import ec.com.model.dao.AdminDao;
import ec.com.model.entity.Admin;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

	@Autowired
	private AdminDao adminDao;

	// 🔐 ログイン処理
	public Admin loginCheck(String email, String password) {
		return adminDao.findByAdminEmailAndAdminPassword(email, password);
	}

	public void loginSuccess(HttpSession session, Admin admin) {
		session.setAttribute("admin", admin);
	}

	public void logout(HttpSession session) {
		session.invalidate();
	}

	// 📝 新規登録処理
	public boolean createAdmin(String adminName, String adminEmail, String adminPassword) {
		if (adminDao.findByAdminEmail(adminEmail) == null) {
			Admin newAdmin = new Admin();
			newAdmin.setAdminName(adminName);
			newAdmin.setAdminEmail(adminEmail);
			newAdmin.setAdminPassword(adminPassword);
			adminDao.save(newAdmin);
			return true;
		} else {
			return false;
		}
	}
}
