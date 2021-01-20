package co.uk.jdreamer.model.persistence.repositories;

import java.util.List;

import co.uk.jdreamer.model.persistence.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
	public List<Item> findByName(String name);

}
