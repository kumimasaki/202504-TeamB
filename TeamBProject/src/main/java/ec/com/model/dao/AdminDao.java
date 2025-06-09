package ec.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.Admin;

@Repository
public interface AdminDao extends JpaRepository<Admin, Long>{
	Admin save(Admin admin);
	Admin findByAccountId(Long adminId);
	Admin findByAdminEmail(String adminEmail);
	Admin findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);
}