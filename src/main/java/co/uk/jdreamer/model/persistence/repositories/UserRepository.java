package co.uk.jdreamer.model.persistence.repositories;

import co.uk.jdreamer.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
