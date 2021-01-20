package co.uk.jdreamer.controllers;

import co.uk.jdreamer.TestUtils;
import co.uk.jdreamer.model.persistence.Cart;
import co.uk.jdreamer.model.persistence.Item;
import co.uk.jdreamer.model.persistence.User;
import co.uk.jdreamer.model.persistence.UserOrder;
import co.uk.jdreamer.model.persistence.repositories.OrderRepository;
import co.uk.jdreamer.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {

        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);

        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findByUsername("someone")).thenReturn(null);

    }

    @Test
    public void testSubmitOrderWithUserNotFound() {

        ResponseEntity<UserOrder> response = orderController.submit("someone");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void testSubmitOrder() {

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertNotNull(order);
        assertEquals(1, order.getItems().size());

    }

    @Test
    public void testGetOrdersForNonExistingUser() {

        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("someone");

        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());

    }

    @Test
    public void testGetOrdersForUser() {

        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("test");

        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());

        List<UserOrder> orders = ordersForUser.getBody();

        assertNotNull(orders);

    }

}