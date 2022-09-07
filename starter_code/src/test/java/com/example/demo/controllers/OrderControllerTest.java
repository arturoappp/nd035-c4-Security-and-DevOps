package com.example.demo.controllers;

import com.example.demo.SareetaApplication;
import com.example.demo.TestObject;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class OrderControllerTest {

    @Autowired
    OrderController orderController;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    private UserRepository userRepository;

    User user = TestObject.stubUser();
    UserOrder userOrder = TestObject.stubUserOrder();

    @Before
    public void setUp() {
        userOrder.setUser(user);
        Cart cart = mock(Cart.class);
        user.setCart(cart);

        when(cart.getItems()).thenReturn(userOrder.getItems());
        when(cart.getTotal()).thenReturn(userOrder.getTotal());
        when(cart.getUser()).thenReturn(user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.save(any())).thenReturn(userOrder);
        when(cartRepository.findByUser(user)).thenReturn(cart);
    }

    @Test
    public void submit() {


        val response = orderController.submit(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userOrder, response.getBody());
    }

    @Test
    public void submit_when_user_dont_exist() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        val response = orderController.submit(user.getUsername());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void getOrdersForUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(TestObject.stubUserOrder()));

        val response = orderController.getOrdersForUser(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }
}