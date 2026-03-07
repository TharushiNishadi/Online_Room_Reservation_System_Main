package com.tharushinis.Ocean_view_resort.service;


import java.util.List;

public interface BaseService<T> {


    T save(T entity) throws Exception;

    boolean update(T entity) throws Exception;

    boolean delete(int id) throws Exception;


    T findById(int id) throws Exception;


    List<T> findAll() throws Exception;


    boolean validate(T entity);
}