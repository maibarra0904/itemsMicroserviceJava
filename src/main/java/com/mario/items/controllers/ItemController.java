package com.mario.items.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mario.items.models.Item;
import com.mario.items.services.ItemService;

@RestController
public class ItemController {

    // @Autowired
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }
    @GetMapping
    public List<Item> list(@RequestParam(name = "name", required = false) String name,
    @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println("Token: " + token);
        System.out.println("Name: " + name);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> detail(@PathVariable Long id) {
        Optional<Item> item = service.findById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity.notFound().build();
    
    }
}
