package com.example.demo.controllers;

import com.example.demo.SareetaApplication;
import com.example.demo.TestObject;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class ItemControllerTest {

    @Autowired
    ItemController itemController;

    @MockBean
    ItemRepository itemRepository;

    @Before
    public void setUp() {
    }

    @Test
    public void getItems() {
        List<Item> items = Arrays.asList(TestObject.stubItem(), TestObject.stubItem());
        when(itemRepository.findAll()).thenReturn(items);

        val response = itemController.getItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getItemById() {
        Item item = TestObject.stubItem();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));

        val response = itemController.getItemById(item.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    public void getItemsByName() {
        Item item = TestObject.stubItem();
        List<Item> items = Arrays.asList(item, item);
        when(itemRepository.findByName(item.getName())).thenReturn(items);

        val response = itemController.getItemsByName(item.getName());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getItemsByName_when_list_is_null() {
        Item item = TestObject.stubItem();
        List<Item> items = Arrays.asList(item, item);
        when(itemRepository.findByName(item.getName())).thenReturn(null);

        val response = itemController.getItemsByName(item.getName());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}