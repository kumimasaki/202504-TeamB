package ec.com.model.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>{
	User save(User user);
	List<User> findAll();
	User findByUserId(Long userId);
	User findByUserEmail(String adminEmail);
	User findByUserEmailAndUserPassword(String userEmail, String userPassword);
}