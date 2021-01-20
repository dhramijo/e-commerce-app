package co.uk.jdreamer.model.persistence.repositories;

import co.uk.jdreamer.model.persistence.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import co.uk.jdreamer.model.persistence.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
