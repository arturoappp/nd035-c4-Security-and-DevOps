package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Log4j2
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("Creating user {}", createUserRequest.getUsername());
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (isInvalidPassword(createUserRequest.getPassword(), createUserRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

        User saveUser = userRepository.save(user);
        return ResponseEntity.ok(saveUser);
    }

    public static boolean isInvalidPassword(String password, String confirmPassword) {
        boolean isInvalid = false;
        log.info("Evaluating password : {}", password);

        if (!password.equals(confirmPassword)) {
            log.error("Pass and conf pass do not match. Unable to create user");
            isInvalid = true;
        }

        if (password.length() > 15 || password.length() < 7) {
            log.error("Password must be less than 15 and more than 8 characters in length.");
            isInvalid = true;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            log.error("Password must have atleast one uppercase character");
            isInvalid = true;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars)) {
            log.error("Password must have atleast one lowercase character");
            isInvalid = true;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            log.error("Password must have atleast one number");
            isInvalid = true;
        }
        String specialChars = "(.*[@,#,$,%].*$)";
//        if (!password.matches(specialChars)) {
//            log.error("Password must have atleast one special character among @#$%");
//            isInvalid = true;
//        }
        return isInvalid;
    }

}
