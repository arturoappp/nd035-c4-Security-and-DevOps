package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TestObject {

    public static User stubUser() {
        User user = new User();
        user.setId((long) randomInt());
        user.setUsername(randomString());

        char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?")).toCharArray();
        String randomStr = RandomStringUtils.random(8, 0, possibleCharacters.length - 1, false, false, possibleCharacters, new SecureRandom());
        randomStr += "Aa1"+randomInt()+"@";


        user.setPassword(randomStr);
        user.setCart(stubCart());
        return user;
    }

    public static Cart stubCart() {
        Cart cart = new Cart();
        cart.setId((long) randomInt());
        cart.setTotal(BigDecimal.valueOf(randomInt()));
        //cart.setItems(new ArrayList<>(Arrays.asList(stubItem(), stubItem())));
        return cart;
    }

    public static Item stubItem() {
        Item item = new Item();
        item.setId((long) randomInt());
        item.setDescription(randomString());
        item.setName(randomString());
        item.setPrice(BigDecimal.valueOf(randomInt()));
        return item;
    }

    public static UserOrder stubUserOrder() {
        UserOrder userOrder = new UserOrder();
        userOrder.setId((long) randomInt());
        userOrder.setItems(new ArrayList<>(Arrays.asList(stubItem(), stubItem())));
        userOrder.setUser(stubUser());
        userOrder.setTotal(BigDecimal.valueOf(randomInt()));
        return userOrder;
    }

    private static int randomInt() {
        return new Random().nextInt(100) + 5;
    }

    private static String randomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
