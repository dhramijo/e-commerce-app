package co.uk.jdreamer.model.persistence.repositories;

import java.util.List;

import co.uk.jdreamer.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import co.uk.jdreamer.model.persistence.User;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
