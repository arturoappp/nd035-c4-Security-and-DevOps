package com.example.demo.controllers;

import com.example.demo.SareetaApplication;
import com.example.demo.TestObject;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.exceptions.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class CartControllerTest {

    @Autowired
    CartController cartController;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    CartRepository cartRepository;

    User user = TestObject.stubUser();
    Item item = TestObject.stubItem();
    Cart cart = TestObject.stubCart();

    @Before
    public void setUp(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
    }

    @Test
    public void addTocart() {

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(item.getId());
        request.setQuantity(2);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);

        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
        assertEquals(2,user.getCart().getItems().size());
    }

    @Test
    public void when_addTocart_and_user_is_null_return_not_found() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(item.getId());
        request.setQuantity(2);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);

        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void when_addTocart_and_item_is_null_return_not_found() {

        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(null));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(item.getId());
        request.setQuantity(2);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);

        assertEquals(HttpStatus.NOT_FOUND, cartResponseEntity.getStatusCode());
    }

    @Test
    public void removeFromcart() {
        user.getCart().addItem(item);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(item.getId());
        request.setQuantity(1);

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(request);

        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
        assertEquals(0,user.getCart().getItems().size());
    }
}