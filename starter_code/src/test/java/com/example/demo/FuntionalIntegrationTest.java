package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
@Transactional
public class FuntionalIntegrationTest {

    @Autowired
    UserController userController;

    @Autowired
    CartController cartController;

    @Autowired
    OrderController orderController;

    User user = TestObject.stubUser();

    @Test
    public void contextLoads() {
        createUserTest();
        user = getUser();
        addTocart();
        submitOrder();
    }

    public void createUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(user.getUsername());
        createUserRequest.setPassword(user.getPassword());
        createUserRequest.setConfirmPassword(user.getPassword());

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        user = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    private User getUser() {
        ResponseEntity<User> response = userController.findByUserName(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUsername(), response.getBody().getUsername());
        return response.getBody();
    }

    public void addTocart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(1);
        request.setQuantity(1);

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        assertEquals(HttpStatus.OK, cartResponseEntity.getStatusCode());
        assertEquals(1, cartResponseEntity.getBody().getItems().size());
    }

    public void submitOrder() {
        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUsername(), response.getBody().getUser().getUsername());
    }
}
