package com.mario.items.services;

import java.util.List;
import java.util.Optional;

import com.mario.items.models.Item;
import com.mario.libs.msvc.commons.entities.Product;


public interface ItemService {

    List<Item> findAll();

    Optional<Item> findById(Long id);

    Product save(Product product);

    Product update(Product product, Long id);

    void delete(Long id);
}
