package com.mario.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mario.items.clients.ProductFeignClient;
import com.mario.items.models.Item;

@Service
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductFeignClient client;

    @Override
    public List<Item> findAll() {
        Random rand = new Random();
        return client.findAll()
                .stream()
                .map(p -> new Item(p, rand.nextInt(10) + 1))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {

        try {
            Random rand = new Random();
            return Optional.of(client.details(id))
                .map(p -> new Item(p, rand.nextInt(10) + 1));
        } catch (Exception e) {
            
            return Optional.empty();
        }

        
        
    }

}
