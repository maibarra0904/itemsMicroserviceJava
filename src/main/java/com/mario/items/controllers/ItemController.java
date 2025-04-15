package com.mario.items.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mario.items.models.Item;
import com.mario.items.models.Product;
import com.mario.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
public class ItemController {

    // @Autowired
    private final ItemService service;
    @SuppressWarnings("rawtypes")
    private final CircuitBreakerFactory circuitBreakerFactory;

    public ItemController(ItemService service, @SuppressWarnings("rawtypes") CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
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
        Optional<Item> item = circuitBreakerFactory.create("items").run(() -> service.findById(id), e -> {
            System.out.println(e.getMessage());

            Product product = new Product();
            product.setCreateAt(LocalDate.now());
            product.setId(1L);
            product.setName("Camara Sony");
            product.setPrice(500.00);
            return Optional.of(new Item(product, 5));
        });
                
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity.notFound().build();
    
    }

     @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct")
        @GetMapping("/details/{id}")
        public ResponseEntity<?> details2(@PathVariable Long id) {
                Optional<Item> itemOptional = service.findById(id);

                if (itemOptional.isPresent()) {
                        return ResponseEntity.ok(itemOptional.get());
                }

                return ResponseEntity.status(404)
                                .body(Collections.singletonMap(
                                                "message",
                                                "No existe el producto en el microservicio msvc-products"));
        }

         @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct2")
        @TimeLimiter(name = "items")
        @GetMapping("/details2/{id}")
        public CompletableFuture<?> details3(@PathVariable Long id) {
                return CompletableFuture.supplyAsync(() -> {
                        Optional<Item> itemOptional = service.findById(id);

                        if (itemOptional.isPresent()) {
                                return ResponseEntity.ok(itemOptional.get());
                        }

                        return ResponseEntity.status(404)
                                        .body(Collections.singletonMap(
                                                        "message",
                                                        "No existe el producto en el microservicio msvc-products"));

                });
        }

        public ResponseEntity<?> getFallBackMethodProduct(Throwable e) {
                System.out.println(e.getMessage());
                

                Product product = new Product();
                product.setCreateAt(
                                LocalDate.now());
                product.setId(1L);
                product.setName("Camara Sony");
                product.setPrice(
                                500.00);
                return ResponseEntity.ok(new Item(product,
                                5));
        }

        public CompletableFuture<?> getFallBackMethodProduct2(Throwable e) {
                return CompletableFuture.supplyAsync(() -> {
                        System.out.println(e.getMessage());
                        

                        Product product = new Product();
                        product.setCreateAt(
                                        LocalDate.now());
                        product.setId(1L);
                        product.setName("Camara Sony");
                        product.setPrice(
                                        500.00);
                        return ResponseEntity.ok(new Item(product,
                                        5));
                });
        }
}
