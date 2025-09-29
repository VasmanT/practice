package com.example.practice.service;

import java.util.List;

public interface ServiceInterface<K, E> {

    E updateById(E entity);
    E getById(K id);
    List<E> getAll();
    void deleteByID(K id);
    E addNew(E entity);
}
