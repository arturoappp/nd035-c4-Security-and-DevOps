package com.example.demo.controllers;

import com.example.demo.SareetaApplication;
import com.example.demo.TestObject;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class UserControllerTest {

    @Autowired
    UserController userController;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    User user = TestObject.stubUser();

    @Before
    public void setUp() {

    }

    @Test
    public void findById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        val response = userController.findById(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByUserName() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        val response = userController.findByUserName(user.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByUserName_when_user_dont_exist() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        val response = userController.findByUserName(user.getUsername());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void createUser() {
        String hashMyPassword = "HashMyPassword";
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(user.getUsername());
        createUserRequest.setPassword(user.getPassword());
        createUserRequest.setConfirmPassword(user.getPassword());

        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(hashMyPassword);
        when(userRepository.save(any())).thenReturn(user);

        user.setPassword(hashMyPassword);

        val response = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertEquals(user.getPassword(), hashMyPassword);
    }

    @Test
    public void createUser_when_password_is_too_short() {
        user.setPassword("short");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(user.getUsername());
        createUserRequest.setPassword(user.getPassword());
        createUserRequest.setConfirmPassword(user.getPassword());

        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("HashMyPassword");
        when(userRepository.save(any())).thenReturn(user);

        val response = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}