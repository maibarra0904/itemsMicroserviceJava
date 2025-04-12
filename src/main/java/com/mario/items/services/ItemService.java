package com.mario.items.services;

import java.util.List;
import java.util.Optional;

import com.mario.items.models.Item;

public interface ItemService {

    List<Item> findAll();

    Optional<Item> findById(Long id);
}
