package com.mario.items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.mario.items.models.Item;
import com.mario.items.models.Product;

@Primary
@Service
public class ItemServiceWebClient implements ItemService {

    private final WebClient.Builder client;
    
    public ItemServiceWebClient(Builder client) {
        this.client = client;
    }

    @Override
    public List<Item> findAll() {
        Random rand = new Random();
        return this.client.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(p -> new Item(p, rand.nextInt(10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        
        Map<String, Long> params = new HashMap<>();

        params.put("id", id);
        Random rand = new Random();

        try {
            return this.client.build()
                .get()
                .uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class)
                .map(p -> new Item(p, rand.nextInt(10) + 1))
                .blockOptional();    
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }

        
    }
    
}
