package co.uk.jdreamer.controllers;

import co.uk.jdreamer.TestUtils;
import co.uk.jdreamer.model.persistence.Item;
import co.uk.jdreamer.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("A Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget description");

        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepo.findByName("A Widget")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void testGetAllItems() {

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals(1, items.size());

    }

    @Test
    public void testGetItemByIdNotFound() {

        ResponseEntity<Item> response = itemController.getItemById(2L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void testGetItemById() {

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item i = response.getBody();

        assertNotNull(i);

    }

    @Test
    public void testGetItemsByNameNotFound() {

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Non-existing Widget");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void testGetItemsByName() {

        ResponseEntity<List<Item>> response = itemController.getItemsByName("A Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals(1, items.size());

    }

}